import React, { Component } from 'react'

export class Control extends Component {
    state = { 
        beerType: "pilsner",
        batchSize: "",
        speed: "",
        validInput: true
    };

    //Handler method for the buttons.
    buttonPress = (e) => {
        //Created a JSON object with "command: {the command stored on the respective button}"
        let data = {
            command: e.target.value
        }

        //If the start button was pressed
        if (data.command === "start") {
            //Set variables as json
            let variables = {
                beerType: this.state.beerType,
                batchSize: this.state.batchSize,
                speed: this.state.speed
            }

            //Fetch the api with the variables as body
            fetch("http://localhost:8080/api/machines/" + this.props.currentMachine.id + "/variables", {
                method: "PUT",
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(variables)
            })
            //Gets the response and sets a variable true or false depending on it
            .then(response => {
                if (response.status !== 200) {
                    this.setState({validInput: false});
                } else {
                    this.setState({validInput: true});
                }
            })

        }

        //Sends the HTTP request to the API
        fetch("http://localhost:8080/api/machines/" + this.props.currentMachine.id + "/command", {
            method: "PUT",
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        })

        //Reset input fields
        this.setState({speed: ""});
        this.setState({batchSize: ""});
    }

    changeBeerType = (e) => {
        this.setState({beerType: e.target.value});
    }

    changeAmount = (e) => {
        this.setState({batchSize: e.target.value});
    }

    changeSpeed = (e) => {
        this.setState({speed: e.target.value});
    }

    //Contains the HTML that is to be rendered for the user
    render() {
        //Set an error message depending on a state variable
        let invalidInputMessage;

        if(this.state.validInput){
            invalidInputMessage = <p></p>
        }else{
            invalidInputMessage = <p>Invalid inputs</p>
        }

        return (
            <div>
                <div>
                    {invalidInputMessage}
                    <form style={formStyle}>
                        <select style={formStyle} onChange={this.changeBeerType}>
                            <option value="pilsner">Pilsner</option>
                            <option value="wheat">Wheat</option>
                            <option value="stout">Stout</option>
                            <option value="ipa">IPA</option>
                            <option value="ale">Ale</option>
                            <option value="alcohol_free">Alcohol Free</option>
                        </select>
 
                        <input style={formStyle} placeholder = "Amount" value={this.state.batchSize} onChange={this.changeAmount}></input>
                        <input style={formStyle} placeholder = "Speed (%)" value={this.state.speed} onChange={this.changeSpeed}></input>

                    </form>
                </div>
                <div>
                    <button 
                        style={btnStyle} 
                        value="start"
                        onClick={this.buttonPress}
                    >
                        Start
                    </button>
                    <button 
                        style={btnStyle} 
                        value="stop"
                        onClick={this.buttonPress}
                    >
                        Stop
                    </button>
                    <button 
                        style={btnStyle} 
                        value="reset"
                        onClick={this.buttonPress}
                    >
                        Reset
                    </button>
                    <button 
                        style={btnStyle} 
                        value="clear"
                        onClick={this.buttonPress}
                    >
                        Clear
                    </button>
                    <button 
                        style={btnStyle} 
                        value="abort"
                        onClick={this.buttonPress}
                    >
                        Abort
                    </button>
                </div>
            </div>
        )
    }
}

//Styling for the buttons
const btnStyle = {
    backgroundColor: "#696969",
    border: "1px solid #000",
    display: "inline-block",
    color: "#fff",
    fontSize: "14px",
    fontWeight: "bold",
    padding: "8px 12px",
    margin: "0px 5px",
    textDecoration: "none",
}


//Styling for the forms
const formStyle = {
    margin: "5px 5px"
}

export default Control
