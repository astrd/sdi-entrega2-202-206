module.exports = function (app, swig, gestorBD) {
    app.get("/home", function (req, res) {

        var respuesta = swig.renderFile('views/home.html', {user: req.session.usuario});
        app.get("logger").info('Usuario se ha dirijido a home');
        res.send(respuesta);


    });
    app.get("/identificarse", function (req, res) {
        var respuesta = swig.renderFile('views/login.html', {});
        res.send(respuesta);
        app.get("logger").info('Usuario se va a identificar');

    });

    app.post("/identificarse", function (req, res) {
        var seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        var criterio = {
            email: req.body.email,
            password: seguro
        }
        gestorBD.obtenerUsuarios(criterio, function (usuarios) {
            if (usuarios == null || usuarios.length == 0) {
                req.session.usuario = null;
                app.get("logger").error('Fallo en autenticacion');
                res.redirect("/identificarse" + "?mensaje=Email o password incorrecto" + "&tipoMensaje=alert-danger ");

            } else {
                req.session.usuario = usuarios[0];
                if (usuarios[0].rol == 'admin') {

                    console.log("admin loged in");
                    res.redirect("/admin");
                    app.get("logger").info('Usuario se ha identificado como admin');
                } else {
                    res.redirect("/home");
                    app.get("logger").info('Usuario Estandar se ha identificado');
                }

            }
        });

    });

    app.get("/registrarse", function (req, res) {
        let respuesta = swig.renderFile('views/signup.html', {});
        res.send(respuesta);
    });

    app.post('/registrarse', function (req, res) {
        console.log(req.body);
        if (req.body.name.length < 2) {
            res.redirect("/registrarse?mensaje=El nombre debe tener entre 2 y 24 caracteres.");
            return;
        }
        if (req.body.email === "" || req.body.email === null) {
            res.redirect("/registrarse?mensaje=El email no puede ser vacío");
            return;
        }
        if (!req.body.email.includes() == "@") {
            res.redirect("/registrarse?mensaje=El email debe contener un @.");
            return;
        }
        if (req.body.password.length < 5) {
            res.redirect("/registrarse?mensaje=La contraseña debe tener entre 5 y 24 caracteres.");
            return;
        }

        if (req.body.surname.length < 2) {
            res.redirect("/registrarse?mensaje=El apellido debe tener entre 5 y 24 caracteres.");
            return;
        }
        if (req.body.password !== req.body.password2) {
            res.redirect("/registrarse?mensaje=Las contraseñas no coinciden.");
            return;
        }
        if (req.body.password2.length < 5) {
            res.redirect("/registrarse?mensaje=La contraseña debe tener entre 5 y 24 caracteres.");
        } else {
            let seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
                .update(req.body.password).digest('hex');
            let buscarUsuario = {
                email: req.body.email
            }
            gestorBD.obtenerUsuarios(buscarUsuario, function (usuarios) {
                if (usuarios != null && usuarios.length !== 0) {
                    res.redirect("/registrarse?mensaje=El email ya está registrado. Inténtelo de nuevo con un email diferente");
                } else {
                    let user = {
                        name: req.body.name,
                        surname: req.body.surname,
                        email: req.body.email,
                        password: seguro,
                        rol: 'user',
                        money: 100.0,
                        valid: true
                    };
                    gestorBD.insertarUsuario(user, function (id) {
                        if (id == null) {
                            app.get("logger").error('Error registro de usuario');
                            res.send("Error al insertar");
                        } else {
                            app.get("logger").info('Usuario se ha registrado');
                            res.redirect("/identificarse");

                        }
                    })
                }
            })
        }
    });

    app.get("/user/list", function (req, res) {
        let userLogged = req.session.usuario;

        if (userLogged.rol === 'user') {
            res.redirect('/home?mensaje=solo el administrador tiene acceso a esta zona');
        } else {
            let criterio = {
                email: {
                    $ne: userLogged.email
                }
            };
            let mysort = (u1, u2) => {
                return u1.email.localeCompare(u2.email);
            };
            gestorBD.obtenerUsuarios(criterio, function (users) {
                if (users == null) {
                    res.redirect("/home?mensaje=Error al listar los usuarios");
                    app.get("logger").error('Error al listar los usuarios');
                } else {
                    let respuesta = swig.renderFile('views/userlist.html',
                        {
                            users: users.sort(mysort)
                        });
                    res.send(respuesta);
                    app.get("logger").info('Administrador se ha dirijido a la vista de usuarios del sistema');
                }
            });
        }
    });

    app.post('/user/delete', function (req, res) {
        let idsUsers = req.body.idsUsers;
        //si es solo un usuario, creamos un array y lo metemos para trabajar con forEach
        if (!Array.isArray(idsUsers)) {
            let aux = idsUsers;
            idsUsers = [];
            idsUsers.push(aux);
        }
        for (let i = 0; i < idsUsers.length; i++) {
            let email = {
                email: idsUsers[i]
            };
            gestorBD.eliminarUsuario(email, function (resultado) {
                if (resultado == null) {
                    res.redirect('/user/list?mensaje=Error al borrar el usuario con email' + user);
                    app.get("logger").error('Error al borrar el usuario');
                } else {
                    if (i === idsUsers.length - 1) {
                        res.redirect('/user/list?mensaje=Usuarios borrados correctamente');
                        app.get("logger").info('Usuarios borrados correctamente');
                    }
                }

            })
        }
    });

    app.get('/desconectarse', function (req, res) {
        req.session.usuario = null;
        console.log("desconectado");
        app.get("logger").info('Usuario se ha desconectado');
        res.redirect("/identificarse");
    });


}
