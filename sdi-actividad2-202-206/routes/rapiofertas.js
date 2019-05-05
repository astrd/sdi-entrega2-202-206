module.exports = function (app, gestorBD) {


    app.post("/api/autenticar/", function (req, res) {
        let seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        let criterio = {
            email: req.body.email,
            password: seguro
        };
        gestorBD.obtenerUsuarios(criterio, function (usuarios) {
            if (usuarios == null || usuarios.length === 0) {
                res.status(401); // Unauthorized
                res.json({
                    autenticado: false
                })
            } else {
                let token = app.get('jwt').sign(
                    {usuario: criterio.email, tiempo: Date.now() / 1000},
                    "secreto");
                res.status(200);
                res.json({
                    autenticado: true,
                    token: token
                })
            }

        });
    });


    app.get("/api/oferta/", function (req, res) {
        let token = req.headers['token'] || req.body.token || req.query.token;
        app.get('jwt').verify(token, 'secreto', function (err, infoToken) {
            if (err) {
                app.get("logger").error('API: Token invalido');
                res.status(403); // Forbidden
                res.json({
                    acceso: false,
                    error: 'Token invalido o caducado'
                });
                // También podríamos comprobar que intoToken.usuario existe
            } else {
                // dejamos correr la petición
                let usuario = infoToken.usuario;
                let cri = {
                    owner: {$ne: res.usuario},
                    state: {$ne: 'no disponible'}
                };
                gestorBD.obtenerOfertas(cri, function (ofertas) {
                    if (ofertas == null || ofertas.length === 0) {
                        app.get("logger").error('Usuario no autorizado');
                        res.status(204); // Unauthorized
                        res.json({
                            err: "No results"
                        });
                    } else {
                        app.get("logger").info('API: Se han mostrado las ofertas disponibles para un usuario');
                        res.status(200);
                        res.send(ofertas);
                    }
                })
            }
        })
    });


    // Segun id de la oferta
    app.post('/api/offer/message/:id', function (req, res) {

        let token = req.headers['token'] || req.body.token || req.query.token;
        app.get('jwt').verify(token, 'secreto', function (err, infoToken) {
            if (err) {
                app.get("logger").error('API: Token invalido');
                res.status(403); // Forbidden
                res.json({
                    acceso: false,
                    error: 'Token invalido o caducado'
                });
            } else {

                let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
                gestorBD.obtenerOfertas(criterio, function (ofertas) {
                    if (ofertas == null || ofertas.length === 0) {
                        res.status(403);
                        app.get("logger").info('API: Se ha producido un error al obtener oferta');
                        res.json({err: "No results"});
                    } else {
                        let usuario = infoToken.usuario;
                        let oferta = ofertas[0];
                        let criterio = {
                            $or: [
                                {
                                    user1: usuario,
                                    user2: ofertas[0].owner,
                                    offer: gestorBD.mongo.ObjectID(req.params.id)
                                },
                                {
                                    user1: ofertas[0].owner,
                                    user2: usuario,
                                    offer: gestorBD.mongo.ObjectID(req.params.id)
                                }
                            ]
                        };

                        //Obtenemos las conversaciones
                        gestorBD.obtenerConversaciones(criterio, function (conversacion) {
                            if (conversacion == null || conversacion.length === 0) {
                                var nuevaConver = {
                                    offer: gestorBD.mongo.ObjectID(oferta._id),
                                    title: oferta.title,
                                    user1: req.body.receiver,
                                    user2: usuario,
                                    valid: true
                                };
                                gestorBD.crearNuevaConversacion(nuevaConver, function (conversacionNueva) {
                                    if (conversacionNueva === null) {
                                        res.status(204);
                                        app.get("logger").info('API: Se ha producido un error al crear conversacion');
                                        res.json({error: "No fue posible crear la conversación"});
                                    } else {
                                        _enviarMensaje(
                                            req.body.message,
                                            usuario,
                                            req.body.receiver,
                                            conversacionNueva,
                                            oferta._id,
                                            req,
                                            res);
                                    }
                                });
                            } else {
                                //si el receptor es nulo consideramos que el propietario esta enviando el mensaje
                                _enviarMensaje(
                                    req.body.message,
                                    usuario,
                                    req.body.receiver,
                                    conversacion[0]._id,
                                    oferta._id,
                                    req, res);
                            }
                        });
                    }
                });
            }
        });
    });


    function _enviarMensaje(text, sender, receiver, convId, offerId, req, res) {

        let message = {
            sender: sender,
            receiver: receiver,
            offer: offerId,
            message: text,
            date: new Date(),
            read: false,
            idConversacion: convId
        };
        gestorBD.insertarMensaje(message, function (mensaje) {
            if (mensaje == null) {
                res.status(500); // error del servidor
                app.get("logger").info('API: Se ha producido un error al insertar el mensaje');
                res.json({
                    err: "Error del servidor"
                });
            } else {
                res.status(200);
                app.get("logger").info('API: El mensaje se ha insertado correctamente');
                res.json(mensaje);
            }
        });

    }


    app.get("/api/offer/conversation/:id", function (req, res) {
        let crit = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.obtenerConversaciones(crit, function (conversaciones) {
            if (conversaciones == null) {
                res.status(500);
                app.get("logger").info('API: Se ha producido un error al obtener conversaciones');
                res.json({error: "Se ha producido un error"});
            } else if (conversaciones.length === 0) {
                res.status(200);
                res.json([]);
            } else {
                let conversacion = conversaciones[0];
                let criterio = {
                    idConversacion: gestorBD.mongo.ObjectID(conversacion._id)
                };
                gestorBD.obtenerMensajes(criterio, function (mensajes) {
                    if (mensajes == null) {
                        res.status(500);
                        app.get("logger").info('API: Se ha producido un error al obtener mensajes');
                        res.json({
                            error: "Ha habido un error"
                        })
                    } else {
                        app.get("logger").info('API: Se ha obtenido el mensaje');
                        res.status(200);
                        res.json(mensajes);
                    }
                });
            }
        });

    });


    app.post("/api/mensaje/eliminar/", function (req, res) {
        let token = req.headers['token'] || req.body.token || req.query.token;
        app.get('jwt').verify(token, 'secreto', function (err, infoToken) {
            if (err) {
                app.get("logger").error('API: Token invalido');
                res.status(403); // Forbidden
                res.json({
                    acceso: false,
                    error: 'Token invalido o caducado'
                });
                // También podríamos comprobar que intoToken.usuario existe
            } else {

                let crit = {"_id": gestorBD.mongo.ObjectID(req.body.id)};
                gestorBD.obtenerOfertas(crit, function (ofertas) {
                    if (ofertas == null) {
                        res.status(500);
                        app.get("logger").info('API: Se ha producido un error al obtener ofertas');
                        res.json({
                            error: "se ha producido un error"
                        })
                    } else if (ofertas.length === 0) {
                        res.status(400);
                        app.get("logger").info('API: No se ha encontrado la oferta');
                        res.json({
                            error: "Oferta no encontrada"
                        })
                    } else {
                        let owner = req.body.receiver;
                        let user = infoToken.usuario;
                        let criterio = {
                            $or: [
                                {
                                    sender: user,
                                    receiver: owner,
                                    offer: gestorBD.mongo.ObjectID(req.body.id)
                                },
                                {
                                    sender: owner,
                                    receiver: user,
                                    offer: gestorBD.mongo.ObjectID(req.body.id)
                                }
                            ]
                        };
                        gestorBD.eliminarMensajes(criterio, function (mensajes) {
                            if (mensajes == null) {
                                res.status(500);
                                app.get("logger").info('API: Se ha producido un error al eliminar mensaje');
                                res.json({
                                    error: "se ha producido un error"
                                })
                            } else {
                                app.get("logger").info('API: Se ha eliminado el mensaje');
                                res.status(200);
                                res.send(mensajes);
                            }
                        });
                    }
                });
            }

        });

    });


    app.get("/api/mensaje/leido/:id", function (req, res) {
        let criterio = {
            "_id": gestorBD.mongo.ObjectID(req.params.id)
        }
        gestorBD.marcarLeido(criterio, function (mensajes) {
            if (mensajes == null) {
                res.status(500);
                app.get("logger").error('API: Se ha producido un error al leer mensajes');

                res.json({
                    error: "se ha producido un error"
                })
            } else {
                res.status(200);
                app.get("logger").info('API: Se ha leido mensaje');
                res.send(mensajes);
            }
        })
    });


    app.post("/api/offer/listConversations", function (req, res) {
        let criterio = {$or: [{user1: res.usuario}, {user2: res.usuario}]};
        gestorBD.obtenerConversaciones(criterio, function (conver) {
            if (conver == null) {
                res.status(500);
                app.get("logger").error("API: Ha habido un error al listar las conversaciones");
                res.json({
                    error: "Se ha producido un error"
                })

            } else {

                app.get("logger").info('API: Se listan las conversaciones correctamente');
                res.send(conver);
            }
        });
    });


    //Borrar una conversacion acuerdo a un criterio
    app.get("/api/conversation/delete/:id", function (req, res) {
        let idOferta = gestorBD.mongo.ObjectID(req.params.id);
        let criterio = {idConversacion: idOferta};

        gestorBD.obtenerMensajes(criterio, function (mensajes) {
            if (mensajes == null) {
                res.status(500);
                app.get("logger").error("API: Se ha producido un error obteniendo los mensajes");
                res.json({
                    error: "se ha producido un error"
                })
            } else if (mensajes.length === 0) {
                res.status(400);
                app.get("logger").error("(API: Se ha producido un error al obtener las ofertas");
                res.json({
                    error: "NO se ha encontrado la oferta"
                })
            } else {
                gestorBD.eliminarMensajes(criterio, function (mensajes) {
                    if (mensajes == null) {
                        res.status(500);
                        app.get("logger").error("API: Se ha producido un error al eliminar los mensajes");
                        res.json({
                            error: "se ha producido un error"
                        })
                    } else {
                        let criterioConversacion = {_id: idOferta};
                        gestorBD.eliminarConversaciones(criterioConversacion, function (mensajes) {
                            if (mensajes == null) {
                                res.status(500);
                                app.get("logger").error("API: Se ha producido un error al eliminar los mensajes");
                                res.json({
                                    error: "se ha producido un error"
                                })
                            } else {
                                res.status(200);
                                app.get("logger").info('API: Se eliminan los mensajes señalados');
                                res.send((mensajes));
                            }
                        });
                    }
                });
            }
        });

    });




    // Buscar conversación por id de oferta y con el token
    app.post('/api/search/offer/conversation/:id', function (req, res) {
        let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
         gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null || ofertas.length === 0) {
                res.status(403);
                app.get("logger").info('API: Se ha producido un error al obtener oferta');
                res.json({
                    acceso: false,
                    error: 'Token invalido o caducado'

                });
            }
            else {

                let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
                 gestorBD.obtenerOfertas(criterio, function (ofertas) {
                    if (ofertas == null || ofertas.length === 0) {
                        res.status(403);
                        app.get("logger").info('API: Se ha producido un error al obtener oferta');
                        res.json({ err: "No results" });
                    }
                    else {
                        let usuario = res.usuario;
                        let oferta = ofertas[0];
                        let criterio = {$or: [
                                {
                                    user1: usuario,
                                    user2: ofertas[0].owner,
                                    offer: gestorBD.mongo.ObjectID(req.params.id)
                                },
                                {
                                    user1: ofertas[0].owner,
                                    user2: usuario,
                                    offer: gestorBD.mongo.ObjectID(req.params.id)
                                }
                            ]};

                        //Obtenemos las conversaciones
                        gestorBD.obtenerConversaciones(criterio, function (conversacion) {
                            if (conversacion == null) {
                                res.status(500);
                                app.get("logger").error("API: Ha habido un error al listar las conversaciones");
                                res.json({ error: "Se ha producido un error" });
                            }
                            else if (conversacion.length === 0) {
                                app.get("logger").error("API: No se ha encontrado la conversacion");

                                var nuevaConver = {
                                    offer: gestorBD.mongo.ObjectID(oferta._id),
                                    title: oferta.title,
                                    user1: oferta.owner,
                                    user2: usuario,
                                    valid: true
                                };
                                gestorBD.crearNuevaConversacion(nuevaConver, function (conversacionNueva) {
                                    if (conversacionNueva === null) {
                                        res.status(204);
                                        app.get("logger").info('API: Se ha producido un error al crear conversacion');
                                        res.json({
                                            error: "No fue posible crear la conversación"
                                        });
                                    } else {
                                        _enviarMensaje(
                                            'Hola! Me interesa su oferta. ',
                                            usuario,
                                            oferta.owner,
                                            conversacionNueva,
                                            oferta._id,
                                            req, res);
                                    }
                                })
                            } else {
                                app.get("logger").info('API: Se listan las conversaciones correctamente');
                                res.send(conversacion[0]);
                            }
                        });
                    }
                });
            }
        });

    });


}
