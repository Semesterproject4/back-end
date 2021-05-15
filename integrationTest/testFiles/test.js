const newman = require('newman');

let errorFlag = false;
newman.run({
    collection: require('./test2.json'),
    reporters: 'cli',
    bail: true
}, function (err) {
    if (err) { 
        errorFlag = true;
        throw err; 
    }
    if(!errorFlag){
        console.log('collection run complete!');
    } else{
        process.exit(1);
    }
});