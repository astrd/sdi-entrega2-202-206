module.exports = function (app, swig,gestorBD) {
    app.get("/identificarse", function (req, res) {
        var respuesta = swig.renderFile('views/login.html', {});
        res.send(respuesta);
    });

    app.get("/registrarse", function (req, res) {
        let respuesta = swig.renderFile('views/signup.html', {});
        res.send(respuesta);
    });

    app.post('/registrarse', function (req, res) {
        console.log(req.body);
        if (req.body.name.length < 2) {
            res.redirect("/registrarse?mensaje=El nombre debe tener entre 2 y 24 caracteres.");
        }
        if (req.body.email === ""|| req.body.email===null) {
            res.redirect("/registrarse?mensaje=El email no puede ser vacío");
        }
        if(!req.body.email.includes()=="@"){
            res.redirect("/registrarse?mensaje=El email debe contener un @.");
        }
        if (req.body.password.length < 5) {
            res.redirect("/registrarse?mensaje=La contraseña debe tener entre 5 y 24 caracteres.");
        }

        if (req.body.surname.length < 2) {
            res.redirect("/registrarse?mensaje=El apellido debe tener entre 5 y 24 caracteres.");
        }
        if (req.body.password !== req.body.password2) {
            res.redirect("/registrarse?mensaje=Las contraseñas no coinciden.");
        }
        if (req.body.password2.length < 5) {
            res.redirect("/registrarse?mensaje=La contraseña debe tener entre 5 y 24 caracteres.");
        } else {
            let seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
                .update(req.body.password).digest('hex');
            let usuario = {
                email: req.body.email,
                password: seguro
            }
            gestorBD.obtenerUsuarios(usuario, function (usuarios) {
                if (usuarios != null && usuarios.length != 0) {
                    res.send("El email ya está registrado. Inténtelo de nuevo con un email diferente");
                    res.redirect("/registrarse");
                } else {
                    let user ={
                        name:req.body.name,
                        surname: req.body.surName,
                        rol:'user',
                        money:100.0
                    }
                    gestorBD.insertarUsuario(usuario, function (id) {
                        if (id == null) {
                            res.send("error al insertar")

                        } else {
                            res.redirect("/identificarse");

                        }
                    })
                }
            })
        }
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
