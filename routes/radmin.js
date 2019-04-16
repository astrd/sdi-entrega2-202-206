module.exports = function (app, swig, gestorBD) {
    app.get("/admin", function (req, res) {
        var respuesta = swig.renderFile('views/admin.html', {});
        res.send(respuesta);
    });
}
