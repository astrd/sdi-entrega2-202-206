module.exports = function(app, swig, gestorBD) {

    app.get("/offer/add", function (req, res) {
        var respuesta = swig.renderFile('views/add.html', {});
        app.get("logger").info('Usuario se ha dirijido a la vista de añadir oferta');

        res.send(respuesta);
    });
    //insertar una oferta desde un formulario
    app.post("/offer/add", function (req, res) {
        if (req.session.usuario == null) {
            res.redirect("/tienda");
            return;
        }
        var cancion = {
            title: req.body.title,
            description: req.body.description,
            price: req.body.price,
            owner: req.session.usuario,
            state: 'disponible'

        }
        gestorBD.insertarOferta(cancion, function (id) {
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
        var respuesta = swig.renderFile('views/search.html', {});
        app.get("logger").info('Usuario se ha dirijido a la vista de buscar oferta');

        res.send(respuesta);
    });
    app.get("/offer/selling", function (req, res) {
        criterio={owner:  req.session.usuario ,
            state: { $ne: 'no disponible' }};
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null) {
                res.send("Error al listar ");
                app.get("logger").error('Error al listar ofertas propias');
            } else {

                var respuesta = swig.renderFile('views/selling.html',
                    {
                        ofertas: ofertas
                    });
                res.send(respuesta);
                app.get("logger").info('Usuario se ha dirijido a la vista ofertas propias');
            }
        });

    });
    app.get("/offer/bought", function (req, res) {
        let criterio={buyer: req.session.usuario.email ,
            state: 'no disponible' };
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null) {
                res.send("Error al listar ");
                app.get("logger").error('Error al listar ofertas');
            } else {

                var respuesta = swig.renderFile('views/bought.html',
                    {
                        ofertas: ofertas
                    });
                res.send(respuesta);
                app.get("logger").info('Usuario se ha dirijido a la vista ofertas compradas');
            }
        });
    });
    app.get("/offer/list", function (req, res) {

        let criterio={owner: { $ne: req.session.usuario },
            state: { $ne: 'no disponible' }};
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null) {
                res.send("Error al listar ");
                app.get("logger").error('Error al listar ofertas');
            } else {

                var respuesta = swig.renderFile('views/list.html',
                    {
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
                        offer: ofertas[0]
                    });
                res.send(respuesta);
            }
        })
    });

    app.get('/offer/buy/:id', function (req, res) {

        var criterio = { _id: gestorBD.mongo.ObjectID(req.params.id)    }
        gestorBD.obtenerOfertas(criterio, function (ofertas) {
            if (ofertas == null) {
                app.get("logger").error('Error al ver detalle de oferta');
                res.send(respuesta);
            } else {
                app.get("logger").info('Usuario se ha dirijido a la vista detallada de oferta');
                var price = ofertas[0].price;
                var sueldo = req.session.usuario.money-price;
                if(sueldo<0)
                {
                    res.redirect('/offer/list?mensaje=Sin sueldo suficiente');
                    app.get("logger").info('Usuario no tiene sueldo suficiente');
                }
                else{
                    var cri ={ "_id": req.session.user._id   }
                    var usuario={   money: 11.1 ,
                    }
                    gestorBD.modificaUsuario(cri , usuario, function (result) {
                        if (result == null) {
                            res.send("Error al modificar usuario");
                            app.get("logger").error('Error al comprar oferta');
                        } else {
                            console.log(cri._id+"........");
                            res.redirect("/home");
                        }
                    });
                }
            }
        })
    });
}
