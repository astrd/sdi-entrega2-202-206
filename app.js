// Módulos
var express = require('express');
var app = express();
var swig = require('swig');


var bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.use(express.static('public'));

// Variables
app.set('port', 8081);


// routerUsuarioSession
var routerUsuarioSession = express.Router();
routerUsuarioSession.use(function(req, res, next) {
    console.log("routerUsuarioSession");
    if ( req.session.usuario ) {
        // dejamos correr la petición
        next();
    } else {
        console.log("va a : "+req.session.destino)
        res.redirect("/identificarse");
    }
});


//Rutas/controladores por lógica
//Rutas/controladores por lógica
require("./routes/rusuarios.js")(app, swig);
require("./routes/rofertas.js")(app, swig);

app.get('/', function (req, res) {
    res.redirect('/home');
});
// lanzar el servid
app.listen(app.get('port'), 8081, function() {
    console.log("Servidor activo");
});
//mongo
