module.exports = function(app, swig) {
    app.get("/identificarse", function (req, res) {
        var respuesta = swig.renderFile('views/login.html', {});
        res.send(respuesta);
    });

    app.get("/registrarse", function (req, res) {
        var respuesta = swig.renderFile('views/signup.html', {});
        res.send(respuesta);
    });

    app.post("/identificarse", function (req, res) {
        var respuesta = swig.renderFile('views/home.html', {});
        res.send(respuesta);
    });

    app.post("/registrarse", function (req, res) {
        var respuesta = swig.renderFile('views/home.html', {});
        res.send(respuesta);
    });
    app.get("/user/:id/details", function (req, res) {
        var respuesta = swig.renderFile('views/userdetails.html', {});
        res.send(respuesta);
    });
    app.get("/user/list", function (req, res) {
        var respuesta = swig.renderFile('views/userlist.html', {});
        res.send(respuesta);
    });

}
