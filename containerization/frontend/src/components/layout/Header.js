import React, { Component } from 'react'
import { Link } from 'react-router-dom'

export class Header extends Component {

    //Styling for the text displaying currentMachine
    textStyle = () => {
        return {
            color: this.props.machine.ip === "none" ? 
            "#fc0303" : "#00bf06"
        }
    }
    
    //Contains the HTML that is to be rendered for the user
    render() {
        return (
            <header style={headerStyle}>
                <h1>BrewMES</h1>
                <p>Current machine: <i style={this.textStyle()}>{this.props.machine.ip}</i></p>
                <div>
                    <Link style={linkStyle} to="/">Home</Link>
                    <Link style={linkStyle} to="/control">Control</Link>
                    <Link style={linkStyle} to="/batch">Batches</Link>
                </div>

            </header>
        )
    }
}

//Styling for the header
const headerStyle = {
    background: "#444444",
    color: "#ffffff",
    textAlign: "center",
}

//Styling of the links
const linkStyle = {
    color: "#ffffff",
    textDecoration: "underline",
    padding: "15px"
}

export default Header


