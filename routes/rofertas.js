module.exports = function(app, swig) {
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
        var respuesta = swig.renderFile('views/list.html', {});
        app.get("logger").info('Usuario se ha dirijido a la vista ofertas disponibles');

        res.send(respuesta);
    });



}
