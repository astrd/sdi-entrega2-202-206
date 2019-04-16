module.exports = function(app, swig, gestorBD) {
    app.get("/home", function (req, res) {
        var respuesta = swig.renderFile('views/home.html', {user: req.session.usuario});
        console.log(req.session.usuario);
        app.get("logger").info('Usuario se ha dirijido a home');
        res.send(respuesta);
    });
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
            owner: req.session.usuario
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
        var respuesta = swig.renderFile('views/selling.html', {});
        app.get("logger").info('Usuario se ha dirijido a la vista de ofertas propias');

        res.send(respuesta);
    });
    app.get("/offer/bought", function (req, res) {
        var respuesta = swig.renderFile('views/bought.html', {});
        app.get("logger").info('Usuario se ha dirijido a la vista de ofertas compradas');

        res.send(respuesta);
    });
    app.get("/offer/list", function (req, res) {

        criterio={};
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



}
