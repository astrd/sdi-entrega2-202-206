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

    app.post('/api/offer/message/:id', function (req, res) {
        let criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null || ofertas.length === 0) {
                res.status(403);
                res.json({
                    err: "No results"
                });
            } else {
                let usuario = res.usuario;
                let oferta = ofertas[0];
                let message = {
                    sender: usuario,
                    receiver: req.body.receiver,
                    offer: oferta._id,
                    message: req.body.message,
                    date: new Date(),
                    read: false
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
                })
            }
        });
    });

    app.get("/api/offer/conversation/:id", function (req, res) {
        let crit = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.obtenerOfertas(crit, function (ofertas) {
            if (ofertas == null) {
                res.status(500);
                app.get("logger").info('API: Se ha producido un error al obtener ofertas');
                res.json({
                    error: "Se ha producido un error"
                })
            } else if (ofertas.length === 0) {
                res.status(400);
                app.get("logger").info('API: Oferta no encontrada');
                res.json({
                    error: "Oferta no encontrada"
                })
            } else {
                let offer = ofertas[0];
                let owner = offer.owner;
                let user = res.usuario;
                let criterio = {
                    $or: [
                        {
                            $and: [
                                {
                                    sender: user
                                },
                                {
                                    receiver: owner.email
                                },
                                {
                                    offer: req.params.id
                                }
                            ]
                        },
                        {
                            $and: [
                                {
                                    sender: owner.email
                                },
                                {
                                  receiver: user
                                },
                                {
                                   offer: req.params.id
                                }
                            ]
                        }
                    ]
                };
                let cri  = {};
                gestorBD.obtenerMensajes(criterio, function (mensajes) {
                    if (mensajes == null) {
                        res.status(500);
                        app.get("logger").info('API: Se ha producido un error al obtener mensajes');
                        res.json({
                            error: "Ha habido un error"
                        })
                    } else {
                        res.status(200);
                        res.json(mensajes);
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

                res.send( mensajes);
            }
        })
    });


};
