module.exports = function (app, swig, gestorBD) {
    app.get("/offer/add", function (req, res) {
        var respuesta = swig.renderFile('views/add.html', {
            user: req.session.user
        });
        app.get("logger").info('Usuario se ha dirijido a la vista de añadir oferta');

        res.send(respuesta);
    });

    //insertar una oferta desde un formulario
    app.post("/offer/add", function (req, res) {
        if (req.session.user == null) {
            res.redirect("/identificarse");
            return;
        }
        if (req.body.title < 2) {
            res.redirect("/offer/add?mensaje=El titulo debe tener entre 2 y 24 caracteres.");
            return;
        }
        if (req.body.description === "" || req.body.description === null) {
            res.redirect("/offer/add?mensaje=La descripcion no puede ser vacío");
            return;
        }
        if (req.body.price === "" || req.body.price === null) {
            res.redirect("/offer/add?mensaje=El precio no puede ser vacío");
            return;
        }
        if (req.body.price === "" || req.body.price === null || req.body.price <= 0) {
            res.redirect("/offer/add?mensaje=Problema en el precio");
            return;
        }

        let oferta = {
            title: req.body.title,
            description: req.body.description,
            price: req.body.price,
            owner: req.session.user.email,
            state: 'disponible',
            buyer: 'none',
            fav: req.body.fav //la hace destacada
        };
        console.log(oferta.fav);
        gestorBD.insertarOferta(oferta, function (id) {
            if (id == null) {
                app.get("logger").error('Error al insertar oferta');
                res.send("Error al insertar oferta");
            } else {
                app.get("logger").info('Se ha añadido oferta');
                res.redirect("/offer/list");
            }
        });
    });

    app.get("/offer/search", function (req, res) {
        var criterio = {};
        if (req.query.busqueda != null) {
            criterio = {
                $or: [
                    {
                        description: {
                            $regex: ".*" + req.query.busqueda + ".*", $options: 'i'
                        }
                    },
                    {
                        title: {
                            $regex: ".*" + req.query.busqueda + ".*", $options: 'i'
                        }
                    }
                ]
            };
        }
        var pg = parseInt(req.query.pg); // Es String !!!
        if (req.query.pg == null) { // Puede no venir el param
            pg = 1;
        }

        gestorBD.obtenerOfertasPg(criterio, pg, function (ofertas, total) {
            if (ofertas == null) {
                res.send("Error al listar ");
            } else {
                let ultimaPg = total / 5;
                if (total % 5 > 0) { // Sobran decimales
                    ultimaPg = ultimaPg + 1;
                }
                let paginas = []; // paginas mostrar
                for (let i = pg - 2; i <= pg + 2; i++) {
                    if (i > 0 && i <= ultimaPg) {
                        paginas.push(i);
                    }
                }
                var respuesta = swig.renderFile('views/search.html',
                    {
                        user: req.session.user,
                        busqueda: req.query.busqueda,
                        ofertas: ofertas,
                        paginas: paginas,
                        ultimaPg: ultimaPg,
                        actual: pg
                    });
                app.get("logger").info('Usuario se ha dirijido a la vista de buscar oferta');
                res.send(respuesta);
            }
        });
    });


    app.get("/offer/selling", function (req, res) {
        let criterio = {
            owner: req.session.user.email,
            state: {$ne: 'no disponible'}
        };
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null) {
                res.send("Error al listar ");
                app.get("logger").error('Error al listar ofertas propias');
            } else {

                var respuesta = swig.renderFile('views/selling.html',
                    {
                        user: req.session.user,
                        ofertas: ofertas
                    });
                res.send(respuesta);
                app.get("logger").info('Usuario se ha dirijido a la vista ofertas propias');
            }
        });

    });
    app.get("/offer/bought", function (req, res) {
        let criterio = {
            state: 'no disponible',
            buyer: req.session.user.email
        };
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null) {
                res.send("Error al listar ");
                app.get("logger").error('Error al listar ofertas');
            } else {

                var respuesta = swig.renderFile('views/bought.html',
                    {
                        ofertas: ofertas,
                        usuario: req.session.user
                    });
                res.send(respuesta);
                app.get("logger").info('Usuario se ha dirijido a la vista ofertas compradas');
            }
        });
    });
    app.get("/offer/list", function (req, res) {

        let criterio = {
            owner: {$ne: req.session.user.email},
            state: {$ne: 'no disponible'}
        };
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null) {
                res.send("Error al listar ");
                app.get("logger").error('Error al listar ofertas');
            } else {
                var respuesta = swig.renderFile('views/list.html',
                    {
                        user: req.session.user,
                        ofertas: ofertas
                    });
                res.send(respuesta);
                app.get("logger").info('Usuario se ha dirijido a la vista ofertas disponibles');
            }
        });

    });

    app.get("/offer/fav", function (req, res) {

        let criterio = {


            fav: 'on',
            owner: {$ne: req.session.user.email},
            state: {$ne: 'no disponible'},

        };
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null) {
                res.send("Error al listar ");
                app.get("logger").error('Error al listar ofertas');
            } else {
                var respuesta = swig.renderFile('views/fav.html',
                    {
                        user: req.session.user,
                        ofertas: ofertas
                    });
                res.send(respuesta);
                app.get("logger").info('Usuario se ha dirijido a la vista ofertas destacadas');
            }
        });

    });

    app.get("/offer/details/:id", function (req, res) {
        var criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null) {
                app.get("logger").error('Error al ver detalle de oferta');
                res.send(respuesta);
            } else {
                app.get("logger").info('Usuario se ha dirijido a la vista detallada de oferta');
                var respuesta = swig.renderFile('views/details.html',
                    {
                        user: req.session.user,
                        offer: ofertas[0]
                    });
                res.send(respuesta);
            }
        })
    });


    app.get('/offer/buy/:id', function (req, res) {

        var criterio = {
            _id: gestorBD.mongo.ObjectID(req.params.id)
        };
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null) {
                app.get("logger").error('Error al ver detalle de oferta');
                res.send(respuesta);
            } else {
                app.get("logger").info('Usuario se ha dirijido a la vista detallada de oferta');
                var price = ofertas[0].price;
                var sueldo = req.session.user.money - price;
                if (sueldo < 0) {
                    res.redirect('/offer/list?mensaje=Sin sueldo suficiente');
                    app.get("logger").info('Usuario no tiene sueldo suficiente');
                } else {
                    var cri = {"_id": gestorBD.mongo.ObjectID(req.session.user._id)}
                    var usuario = {
                        name: req.session.user.name,
                        surname: req.session.user.surname,
                        email: req.session.user.email,
                        password: req.session.user.password,
                        rol: 'user',
                        money: sueldo,
                        valid: true
                    }
                    gestorBD.modificaUsuario(cri, usuario, function (result) {
                        if (result == null) {
                            res.send("Error al modificar usuario");
                            app.get("logger").error('Error al modificar usuario');
                        } else {
                            var crit = {"_id": gestorBD.mongo.ObjectID(req.params.id)}
                            var oferta = {
                                title: ofertas[0].title,
                                description: ofertas[0].description,
                                price: ofertas[0].price,
                                owner: ofertas[0].owner,
                                state: 'no disponible',
                                buyer: req.session.user.email
                            }
                            gestorBD.modificarOferta(crit, oferta, function (result) {
                                if (result == null) {
                                    res.send("Error al modificar oferta");
                                    app.get("logger").error('Error al comprar oferta');
                                } else {
                                    req.session.user = usuario;
                                    app.get("logger").error('Usuario ha comprado oferta');
                                    res.redirect('/offer/bought');
                                }
                            });
                        }
                    });
                }
            }
        })
    });

    app.get('/offer/delete/:id', function (req, res) {
        var criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.eliminarOferta(criterio, function (ofertas) {
            if (ofertas == null) {
                app.get("logger").error('Error al borrar la oferta');
                res.redirect("/offer/selling?mensaje=Error al borrar la oferta");
            } else {
                app.get("logger").error('Éxito al borrar la oferta');
                res.redirect("/offer/selling?mensaje=Se ha eliminado correctamente la oferta");

            }
        });
    });

    app.get('/offer/fav/:id', function (req, res) {
        var criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null) {
                app.get("logger").error('Error al destacar la oferta');
                res.redirect("/offer/selling?mensaje=Error al destacar la oferta");
            } else {
                let sueldo = req.session.user.money - 20;//coste de destacar la oferta
                if (sueldo < 0) {
                    res.redirect('/offer/list?mensaje=Sin sueldo suficiente para destacar oferta');
                    app.get("logger").info('Usuario no tiene sueldo suficiente');
                } else {
                    let cri = {"_id": gestorBD.mongo.ObjectID(req.session.user._id)}
                    let usuario = {
                        name: req.session.user.name,
                        surname: req.session.user.surname,
                        email: req.session.user.email,
                        password: req.session.user.password,
                        rol: 'user',
                        money: sueldo,
                        valid: true
                    }
                    gestorBD.modificaUsuario(cri, usuario, function (result) {
                        if (result == null) {
                            res.send("Error al modificar usuario");
                            app.get("logger").error('Error al modificar usuario');
                        } else {
                            var crit = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
                            var oferta = {
                                title: ofertas[0].title,
                                description: ofertas[0].description,
                                price: ofertas[0].price,
                                owner: ofertas[0].owner,
                                fav:'on'
                            };
                            gestorBD.modificarOferta(crit, oferta, function (result) {
                                if (result == null) {
                                    res.send("Error al modificar oferta");
                                    app.get("logger").error('Error al modificar oferta');
                                } else {
                                    req.session.user = usuario;
                                    app.get("logger").error('Usuario ha destacado oferta');
                                    res.redirect('/offer/selling');
                                }
                            });
                        }
                    });


                }
            }
        });

    });
};
