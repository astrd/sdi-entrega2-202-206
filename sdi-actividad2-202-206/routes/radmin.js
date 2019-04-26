module.exports = function (app, swig, gestorBD) {
    app.get("/admin", function (req, res) {
        var respuesta = swig.renderFile('views/admin.html', {
            user: req.session.user
        });
        app.get("logger").info('Admin se ha dirijido a la pagina de administracion principal');
        res.send(respuesta);
    });


};
