import React, { Component } from 'react'
import { Icon } from '@iconify/react-with-api';

export class Batches extends Component {
    //State contains all the variables of the class
    

    state = {
        searchVar: "",
        selectedBatchID: "",
        selectSuccess: false,
        errorMessage: "",
        link: "",
        Pagebatches: [],
        selectedBatch: "", 
        page: 0,
        maxpage: 0,
    };

    componentDidMount(){
        this.getBatches(0);
    }

    search = (e) => {
        fetch('http://localhost:8080/api/batches/' + document.getElementById("searchField").value)
            .then(response => {
                let json = response.json();
                json.then(data => {
                    if (response.status === 200) {
                        this.setState({ selectedBatchID: data.id, selectSuccess: true, errorMessage: "", link: "http://localhost:8080/api/batches/" + data.id + "/get-report"});
                    } else if (response.status === 400) {
                        this.setState({ selectedBatchID: "", selectSuccess: false, errorMessage: "Something went wrong, have you entered a valid UUID?", link: "" })
                    } else {
                        this.setState({ selectedBatchID: "", selectSuccess: false, errorMessage: data.response, link: "" });
                    }
                })
            }
            )

    }

    change = (e) => {
        this.setState({selectedBatch: e.target.value
    });
}



    generatePDF = (e) => {
        if(e.target.value === "search"){
            window.location.href = this.state.link;
        } else if(e.target.value === "pages"){
            window.location.href = "http://localhost:8080/api/batches/" + this.state.selectedBatch + "/get-report"
        }
    }

    getBatches(page){
        this.setState({
            Pagebatches: []
        });
        console.log("fetching.. ", this.state.page)
        this.setState({page: page})
        fetch('http://localhost:8080/api/batches?page=' + page +'&size=10')
        .then(response => {
            if(response.status === 200){
                response.json().then(data => {
                    data.batches.forEach(element => {
                        this.setState({
                            Pagebatches: [...this.state.Pagebatches, {id:element.id}]
                        });
                    });
                    this.setState({maxpage: data.totalPages-1});
                })
            }
        })
    }


    updatePage = (e) => {
        if(e.target.value === "prev"){
            if(this.state.page > 0){
                let page = this.state.page - 1;
                console.log("Current page:", this.state.page);
                console.log("New page:", page);
                this.getBatches(page);
            } 
        } else if(e.target.value === "next"){
            if(this.state.page < this.state.maxpage){
                let page = this.state.page + 1;
                console.log("Current page:", this.state.page);
                console.log("New page:", page);
                this.getBatches(page);
            } 
        }
    }

    render() {
        let selectedBatchMessage;
        let errorMessage;

        if (this.state.selectSuccess) {
            selectedBatchMessage = <p>Selected batch: {this.state.selectedBatchID}</p>
            errorMessage = <p></p>
        } else {
            selectedBatchMessage = <p>Selected batch: none</p>
            errorMessage = <p style={{ color: "red" }}>{this.state.errorMessage}</p>
        }

        return (
            <div>
                <div>
                    {selectedBatchMessage}
                </div>
                <div>
                    {errorMessage}
                </div>
                <div>
                    <input style={inputStyle} id="searchField" placeholder="Batch ID"></input>
                    <button style={btnStyle} onClick={this.search}>Search</button>
                </div>
                <div style={{padding:"10px"}}>
                    {this.state.selectSuccess === true ? (<button value="search" style={btnStyle} onClick={this.generatePDF}>Generate Report</button>) : (<p></p>)}
                </div>
                <div style={{margin:"25px"}}>
                <div>
                    <h1>All batches</h1>
                </div>
                    <select 
                            size="10"
                            onChange={this.change}
                            style={selectStyle}
                    >
                        {this.state.Pagebatches.map((option) => (
                            <option style={optionStyle}
                                value={option.id}
                                key={option.id}
                            >
                                {option.id} 
                            </option>
                            
                        ))}
                    </select>
                    <div>
                        <p style={{fontSize: "20px"}}>{this.state.page+1} of {this.state.maxpage+1}</p>
                    </div>
                    <div>
                        <button style={btnStyle} onClick={this.updatePage} value="prev">&lt;prev</button>
                        <button style={btnStyle} onClick={this.updatePage} value="next">&gt;next</button>
                    
                    </div>
                    <div style={{padding:"10px"}}>
                        <button value="pages" style={btnStyle} onClick={this.generatePDF}>Generate Report</button>
                    </div>
                </div>
            </div>
        )
    }
}
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


const inputStyle =   {
    width: "50%",
    padding: "12px 20px",
    margin: "8px 0",
    boxSizing: "border-box",
    border: "none",
    borderBottom: "4px solid grey"
  }

  const selectStyle = {
    overflow: "hidden",
    height: "470px", 
    width: "60%",
    textAlign: "center", 
    fontSize: "26px",
    boxSizing: "border-box",
    border: "hidden"
  }

  const optionStyle = {
    padding: "8px", 
    outlineStyle:"solid 1px", 
    backgroundColor: "#D0D0D0"
  }

  const pageButtonStyle = {
    textDecoration: "none",
   display: "inline-block",
   padding: "10px",
   fontSize: "26px",
   width: "3%",
   textAlign: "center",
   borderRadius: "100%"
}
  
export default Batches