const newman = require('newman');

let errorFlag = false;
newman.run({
    collection: require('./newTest.json'),
    reporters: 'cli',
    iterationCount: 50,
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
