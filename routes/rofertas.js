module.exports = function(app, swig) {
    app.get("/home", function (req, res) {
        var respuesta = swig.renderFile('views/home.html', {user: req.session.usuario});
        console.log(req.session.usuario);
        res.send(respuesta);
    });
    app.get("/offer/add", function (req, res) {
        var respuesta = swig.renderFile('views/add.html', {});
        res.send(respuesta);
    });
    app.get("/offer/search", function (req, res) {
        var respuesta = swig.renderFile('views/search.html', {});
        res.send(respuesta);
    });
    app.get("/offer/selling", function (req, res) {
        var respuesta = swig.renderFile('views/selling.html', {});
        res.send(respuesta);
    });
    app.get("/offer/bought", function (req, res) {
        var respuesta = swig.renderFile('views/bought.html', {});
        res.send(respuesta);
    });
    app.get("/offer/list", function (req, res) {
        var respuesta = swig.renderFile('views/list.html', {});
        res.send(respuesta);
    });



}
