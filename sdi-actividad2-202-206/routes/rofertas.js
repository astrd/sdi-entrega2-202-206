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
        let oferta = {
            title: req.body.title,
            description: req.body.description,
            price: req.body.price,
            owner: req.session.user.email,
            state: 'disponible',
            buyer: 'none',
        };
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
        var respuesta = swig.renderFile('views/search.html', {
            user: req.session.user
        });
        app.get("logger").info('Usuario se ha dirijido a la vista de buscar oferta');

        res.send(respuesta);
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
        let criterio={
            state:'no disponible',
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
                var sueldo = req.session.user.money-price;
                if(sueldo<0)
                {
                    res.redirect('/offer/list?mensaje=Sin sueldo suficiente');
                    app.get("logger").info('Usuario no tiene sueldo suficiente');
                }
                else{
                    var cri ={ "_id": gestorBD.mongo.ObjectID(req.session.user._id )  }
                    var usuario={
                        name: req.session.user.name,
                        surname: req.session.user.surname,
                        email: req.session.user.email,
                        password: req.session.user.password,
                        rol: 'user',
                        money: sueldo,
                        valid: true
                    }
                    gestorBD.modificaUsuario(cri , usuario, function (result) {
                        if (result == null) {
                            res.send("Error al modificar usuario");
                            app.get("logger").error('Error al comprar oferta');
                        } else {
                            console.log(req.session.user.money+"........"+sueldo);

                            var crit ={ "_id": gestorBD.mongo.ObjectID(req.params.id)  }
                            var oferta={
                                title: ofertas[0].title,
                                description: ofertas[0].description,
                                price: ofertas[0].price,
                                owner: ofertas[0].owner,
                                state: 'no disponible',
                                buyer: req.session.user.email
                            }
                            gestorBD.modificarOferta(crit , oferta, function (result) {
                                if (result == null) {
                                    res.send("Error al modificar usuario");
                                    app.get("logger").error('Error al comprar oferta');
                                } else {
                                    console.log(ofertas[0].state+"...."+oferta.buyer+"...."+ofertas[0].buyer);
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
};
