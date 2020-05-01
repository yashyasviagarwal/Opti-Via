const express = require("express");
const app = express();
const fetch = require("node-fetch");
const geocoder = require("geocoder");
const cors = require("cors");
const bodyParser = require("body-parser");
const { PythonShell } = require("python-shell");


app.use(cors());

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.get("/", (req, res) => {
  res.send("Works Here");
});

app.post("/map", (req, res) => {
  const { start_lat, start_long, end_lat, end_long } = req.body;
  // console.log(start_lat,start_long,end_lat,end_long);
  let options = {
    //comment to test the demo response
    args: [start_lat, start_long, end_lat, end_long]
    // uncomment to test the demo response
    // args: ['-8.2203067','54.3604272', '-7.6255142','54.7088763']
  };
  PythonShell.run("model2.py", options, (err, result) => {
    if (err) {
      throw err;
    }
    console.log('results: %j',result);
    try{
        res.send(result[0]);
    }
    catch(err){
      res.send(err);
    }
  });
});


app.post("/sos", (req, res) => {
  const { sos_lat, sos_long } = req.body;
  let options = {
    //comment to test the demo response
    args: [sos_lat, sos_long]
  };
  PythonShell.run("emergency.py", options, (err, result) => {
    if (err) {
      throw err;
    }
    console.log('results: %j',result);
    try{
        res.send(result[0]);
    }
    catch(err){
      res.send(err);
    }
  });
});





app.post("/geocode", (req, res) => {
  const { location } = req.body;
  geocoder.geocode(`location`, function(err, data) {
    if (err) {
      res.send(err);
    } else {
      res.send(data);
    }
  });
});
var port = process.env.PORT || 7777;
app.listen(port, () => {
  console.log("Server started on:",port);
});