module.exports = function (app, swig, gestorBD) {
    app.get("/admin", function (req, res) {
        var respuesta = swig.renderFile('views/admin.html', {});
        app.get("logger").info('Admin se ha dirijido a la pagina de administracion principal');
        res.send(respuesta);
    });

    app.get("/user/:id/details", function (req, res) {
        var respuesta = swig.renderFile('views/userdetails.html', {});
        app.get("logger").info('Admin se ha dirijido los detalles de un usuario');
        res.send(respuesta);
    });
    app.get("/user/list", function (req, res) {
        var respuesta = swig.renderFile('views/userlist.html', {});
        app.get("logger").info('Admin se ha dirijido a administrar usuarios');
        res.send(respuesta);
    });


}
