import React, { Component } from 'react'
import { Link } from 'react-router-dom'

export class MachineList extends Component {
    //State contains all the variables of the class
    state = { 
        machines: [],
        selectedMachine: {
            ip: "default",
            id: "default"
        },
        machineIP: "",
        success: true,
        statusMessage: ""
    };


    //Is forced to complete before render()
    componentDidMount() {
        this.updateMachineList();
    }

    updateMachineList = () =>{
        this.setState({machines: []})

        fetch('http://localhost:8080/api/machines')
        .then(response => {
            if (response.status === 200) {
                response.json().then(data => {
                    //Loops through and add all machines to the list of machines
                    data.forEach(element => {
                        this.setState({ machines: [...this.state.machines, {label: element.ip, value: element.id}] });
                    });

                    //Sets first machine as default as the list will have it selected initially
                    if (this.state.machines.length !== 0) {
                        this.setState({selectedMachine: {
                            ip: this.state.machines[0].label,
                            id: this.state.machines[0].value
                        }})
                    }
                })
            }
        });
    }

    addMachineHandler = (e) =>{
        let data = {ip: this.state.machineIP};

        fetch("http://localhost:8080/api/machines/",{
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        }).then(response => {
            if(response.status !== 200){
                this.setState({success: false});
            }else{
                this.setState({success: true});
                this.updateMachineList();
            }

            return response.text();
        }).then(data => JSON.parse(data))
        .then(json => this.setState({statusMessage: json.response, machineIP: ""}));
    }

    ipChanged = (e) =>{
        this.setState({machineIP: e.target.value});
    }

    //When button is pressed we send the 
    selectMachineHandler = () => {
        //Sends the current machine to App.js
        this.props.setCurrentMachine(this.state.selectedMachine)
    }

    //Handler for the selection of a new machine in the list
    change = (e) => {
        let selectedJSON = JSON.parse(e.target.value)

        this.setState({selectedMachine: {
            ip: selectedJSON.label,
            id: selectedJSON.value
        }})
    }
    
    //Contains the HTML that is to be rendered for the user
    render() {
        let errorMessage;

        if(this.state.success){
            errorMessage = <p></p>
        }else{
            errorMessage = <p>{this.state.statusMessage}</p>
        }

        return (
            <div>
                <form>
                    <input placeholder = "opc.tcp://<ip adress>:<port>" value = {this.state.machineIP} onChange = {this.ipChanged} style={inputStyle}></input>
                    <button onClick={this.addMachineHandler} style={btnStyle}>Add machine</button>
                </form>
                {errorMessage}

                <h1>All machines</h1>

                <select style={selectStyle}
                        size="10"
                        onChange={this.change}
                >
                    {this.state.machines.map((option) => (
                        <option 
                            style={optionStyle}
                            value={JSON.stringify(option)}
                            key={option.value}
                        >
                            {option.label} 
                        </option>
                    ))}
                </select>

                <br></br>

                <Link to="/control">
                    <button onClick={this.selectMachineHandler} style={btnStyle}>Connect</button>
                </Link> 
                
            </div>
        )
    }
}

//Styling of the button
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
    width: "10%"
}

const selectStyle = {
    height: "580px", 
    width: "60%",
    textAlign: "center", 
    fontSize: "26px",
    boxSizing: "content-box",
    border: "hidden"
  }

  const optionStyle = {
    padding: "8px", 
    backgroundColor: "#D0D0D0"
  }

  const inputStyle =   {
    width: "50%",
    padding: "12px 20px",
    margin: "8px 0",
    boxSizing: "border-box",
    border: "none",
    borderBottom: "4px solid grey"
  }

export default MachineList
