import React, { Component } from 'react'
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import { Icon } from '@iconify/react-with-api';



export class Liveview extends Component {

    state = { 
        machineID: "",
        socket: null,
        stompClient: null, 
        livedata: {
            barley: 0,
            hops: 0,
            malt: 0,
            wheat: 0,
            yeast: 0,
            maintenance: 0,
            humidity: 0.0,
            vibration: 0.0,
            temperature: 0.0,
            amountToProduce: 0,
            totalProducts: 0,
            acceptableProducts: 0,
            defectProducts: 0,
            batchID: 0,
            speed: 0,
            beerType: 0,
            currentState: 0
        }
    };

    componentDidMount() {
        //Initialize socket and stomp client.
        let socket = new SockJS('http://localhost:8080/websocket')
        let stompClient = Stomp.over(socket);
        stompClient.debug = null

        //Save machineID -> then socket -> then stomp client -> then connect
        this.setState({machineID: this.props.currentMachine.id}, () => 
            this.setState({socket: socket}, () => 
                this.setState({stompClient: stompClient}, () => 
                    this.connect(this.state.machineID, this.storeData)
                )
            )
        );
    }
    componentWillUnmount(){
        console.log("Leaving liveview")
        let socket = this.state.socket;
        socket.close();
    }

    
    //Connect method that subscribes to the live data
    connect(id, func) {
        let stompClient = this.state.stompClient
        //Start connection
        stompClient.connect({}, function (frame) {
            //Send initial info to backend to start the sending of data
            stompClient.send("/app/connect/" + id, {}, JSON.stringify({'name': "filler value"})); 
            //Start subscription (when we get data we send random data which triggers the backend to send data)
            //Essentially we just loop through this forever.
            stompClient.subscribe('/topic/' + id + '/livedata', function (data) {
                //Send new data to function storeData
                func(data.body)
                //Send random info to backend to "trigger" it to send a few set of data.
                stompClient.send("/app/connect/" + id, {}, JSON.stringify({'name': "filler value"})); 
            });
        });
    }

    //Convert data to json and store it in the state variable
    storeData = (data) => {
        let json = JSON.parse(data);
        console.log(json)
        if (this.state.livedata !== json)
            this.setState({livedata: json});
    }

    render() {
        return (
            <div>
                <div style={{display: "inline-flex", align: "center", padding: "10px"}}>
                    <div style={{padding: "0px 10px"}}>
                        <label for="barley">Barley</label> <br></br>
                        <progress id="barley" value={this.state.livedata.barley} max="35000"></progress> <br></br>
                        <p>{this.state.livedata.barley}</p>
                    </div>
                    <div style={{padding: "0px 10px"}}>
                        <label for="hops">Hops</label> <br></br>
                        <progress id="hops" value={this.state.livedata.barley} max="35000"></progress> <br></br>
                        {this.state.livedata.hops}
                    </div>
                    <div style={{padding: "0px 10px"}}>
                        <label for="malt">Malt</label> <br></br>
                        <progress id="malt" value={this.state.livedata.barley} max="35000"></progress> <br></br>
                        {this.state.livedata.malt}
                    </div>
                    <div style={{padding: "0px 10px"}}>
                        <label for="wheat">Wheat</label> <br></br>
                        <progress id="wheat" value={this.state.livedata.barley} max="35000"></progress> <br></br>
                        {this.state.livedata.wheat}
                    </div>
                    <div style={{padding: "0px 10px"}}>
                        <label for="yeast">Yeast</label> <br></br>
                        <progress id="yeast" value={this.state.livedata.barley} max="35000"></progress> <br></br>
                        {this.state.livedata.yeast}
                    </div>
                </div>
                <br></br>
                <div style={{display: "inline-flex", align: "center", padding: "10px"}}>
                    <div style={{padding: "0px 25px"}}>
                        <Icon icon="carbon-humidity" style={{width: "80px", height: "80px", color: "#42a7f5"}}/>
                        <p>Humidity</p>
                        <h1>{this.state.livedata.humidity}</h1>
                    </div>

                    <div style={{padding: "0px 25px"}}>
                        <Icon icon="ph-vibrate" style={{width: "80px", height: "80px"}}/>
                        <p>Vibration</p>
                        <h1>{this.state.livedata.vibration}</h1>
                    </div>

                    <div style={{padding: "0px 25px"}}>
                        <Icon icon="emojione-v1:thermometer" style={{width: "80px", height: "80px"}}/>
                        <p>Temperature</p>
                        <h1>{this.state.livedata.temperature}</h1>
                    </div>
                    <div style={{padding: "0px 25px"}}>
                        <Icon icon="cil-speedometer" style={{width: "80px", height: "80px"}}/>
                        <p>Speed</p>
                        <h1>{Math.round(this.state.livedata.speed)}%</h1>
                    </div>
                    <div style={{padding: "0px 25px"}}>
                        <Icon icon="mdi-state-machine" style={{width: "80px", height: "80px"}}/>
                        <p>State</p>
                        <h1>{this.state.livedata.currentState}</h1>
                    </div>
                </div>
                <br></br>
                <div style={{display: "inline-flex", align: "center", padding: "10px"}}>
                    <div style={{padding: "0px 25px"}}>
                        <Icon icon="jam-bottle-f" style={{width: "80px", height: "80px"}}/>
                        <p>Beer type</p>
                        <h1>{this.state.livedata.beerType}</h1>
                    </div>
                    <div style={{padding: "0px 25px"}}>
                        <Icon icon="bx-bxs-flag-checkered" style={{width: "80px", height: "80px"}}/> <br></br>
                        <p>Amount to produce</p>
                        <h1>{this.state.livedata.amountToProduce}</h1>
                    </div>

                    <div style={{padding: "0px 25px"}}>
                        <Icon icon="jam-bottle" style={{width: "80px", height: "80px", color: "#148432"}}/>
                        <p>Produced</p>
                        <h1>{this.state.livedata.totalProducts}</h1>
                    </div>

                    <div style={{padding: "0px 25px"}}>
                        <Icon icon="subway-tick" style={{width: "80px", height: "80px", color: "#11a839"}}/>
                        <p>Acceptable</p>
                        <h1>{this.state.livedata.acceptableProducts}</h1>
                    </div>

                    <div style={{padding: "0px 25px"}}>
                        <Icon icon="emojione-cross-mark" style={{width: "80px", height: "80px"}}/>
                        <p>Defect</p>
                        <h1>{this.state.livedata.defectProducts}</h1>
                    </div>
                </div>
                <br></br>
                <div style={{display: "inline-flex", align: "center", padding: "10px"}}>
                    <div style={{padding: "0px 10px"}}>
                        <label for="maintenance">Maintenance</label> <br></br>
                        <progress id="maintenance" style={{width: "680px", height: "40px"}} value={this.state.livedata.maintenance} max="30000"></progress> <br></br>
                        <p>{(this.state.livedata.maintenance / 30000 * 100).toFixed(1)}%</p>
                    </div>
                </div>
                <br></br>
                <div style={{display: "inline-flex", align: "center", padding: "10px"}}>
                    <div style={{padding: "0px 25px"}}>
                        <Icon icon="mdi-archive" style={{width: "80px", height: "80px", color: "#dbd202"}}/>
                        <p>Batch id</p>
                        <h1>{this.state.livedata.batchID}</h1>
                    </div>
                </div>
            </div>
        )
    }
}



export default Liveview
