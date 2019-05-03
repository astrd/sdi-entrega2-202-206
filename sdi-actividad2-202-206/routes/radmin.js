module.exports = function (app, swig, gestorBD) {
    app.get("/admin", function (req, res) {
        var respuesta = swig.renderFile('views/admin.html', {
            user: req.session.user
        });
        app.get("logger").info('Admin se ha dirijido a la pagina de administracion principal');
        res.send(respuesta);
    });

    app.get("/resetdb", function (req, res) {
        gestorBD.resetDB();
        app.get("logger").info('Admin ha reseteado la base de datos');
        res.redirect('/addadmin');



    });
    app.get("/addadmin", function (req, res) {
        var seguro = app.get("crypto").createHmac('sha256', app.get('clave')).update('admin').digest('hex');
        var cri = {email: 'admin@email.com'  };
        gestorBD.obtenerUsuarios(cri, function (usuarios) {
            if (usuarios != null && usuarios.length !== 0) {
                res.redirect("/identificarse?mensaje=Admin ya existe"+ "&tipoMensaje=alert-danger ");
            } else {
                var admin = {
                    name: 'admin',
                    surname: 'admin',
                    email: 'admin@email.com',
                    password: seguro,
                    rol: 'admin',
                    money: 100.0,
                    valid: true
                };
                gestorBD.insertarUsuario(admin, function (id) {
                    if (id == null) {
                        app.get("logger").error('Error registro de admin');
                        res.send("Error al insertar admin en reset");
                    } else {
                        app.get("logger").info('Usuario admin creado');
                        res.redirect("/identificarse?mensaje=Admin Creado");

                    }
                })
            }
        })
    });
};
