module.exports = function(app, gestorBD) {
    app.post("/api/autenticar/", function(req, res) {
        var seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        var criterio = {
            email : req.body.email,
            password : seguro
        }

        gestorBD.obtenerUsuarios(criterio, function(usuarios) {
            if (usuarios == null || usuarios.length == 0) {
                res.status(401); // Unauthorized
                res.json({
                    autenticado : false
                })
            } else {
                var token = app.get('jwt').sign(
                    {usuario: criterio.email , tiempo: Date.now()/1000},
                    "secreto");
                res.status(200);
                res.json({
                    autenticado : true,
                    token : token
                })
            }

        });
    });
    app.get("/api/oferta", function(req, res) {
        cri = { owner: {$ne: res.usuario},
            state: {$ne: 'no disponible'} }

        console.log(res.usuario);
        gestorBD.obtenerOfertas( cri , function(ofertas) {
            if (ofertas == null || ofertas.length== 0) {
                res.status(403);
                res.json({
                    error : "se ha producido un error"
                })
            } else {
                res.status(200);
                res.send( JSON.stringify(ofertas) );
            }
        });
    });
}
