'use strict'

const locationsHTML = document.getElementById('locations');

function createLocation(locationValue, locationIndex) {

    const locationHTML = document.createElement("div");
    locationHTML.classList.add("location");

    const roomHTML = document.createElement("h2");
    roomHTML.classList.add("room");
    roomHTML.innerHTML = locationValue.room;
    locationHTML.append(roomHTML);

    const listElementsHTML = document.createElement("ul");
    listElementsHTML.classList.add("elements");

    const policyHTML = document.createElement("li");
    policyHTML.classList.add("element");
    policyHTML.classList.add("policy");
    const policyValue = locationValue.policy.active;
    if(policyValue) {
        policyHTML.classList.add("active");
    } else {
        policyHTML.classList.add("inactive");
    }
    console.log("Policy value: " + policyValue)
    const policyImgHTML = new Image();
    policyImgHTML.src = './img/policy-manager.png';
    policyHTML.append(policyImgHTML);
    const sweetNightPolicyImgHTML = new Image();
    sweetNightPolicyImgHTML.src = './img/sweet-night.png';
    sweetNightPolicyImgHTML.classList.add("sweet-night");
    const sweetNightPolicyValue = locationValue.policy.sweet_night
    if(sweetNightPolicyValue) {
        sweetNightPolicyImgHTML.classList.add("active");
    } else {
        sweetNightPolicyImgHTML.classList.add("inactive");
    }
    policyHTML.append(sweetNightPolicyImgHTML);
    listElementsHTML.append(policyHTML);

    let deviceId;
    for(deviceId in locationValue.devices) {
        console.log(deviceId);
        const deviceValue = locationValue.devices[deviceId];
        const deviceHTML = document.createElement("li");
        deviceHTML.classList.add("element");
        deviceHTML.classList.add("device");
        console.log(deviceValue.type);
        let deviceImgHTML;
        switch(deviceValue.type) {
            case "presence_sensor":
                deviceHTML.classList.add("presence-sensor");
                console.log("Presence: " + deviceValue.presence);
                if(deviceValue.presence) {
                    deviceHTML.classList.add("presence");
                } else {
                    deviceHTML.classList.add("no-presence");
                }
                deviceImgHTML = new Image();
                deviceImgHTML.src = './img/presence-sensor.png';
                deviceHTML.append(deviceImgHTML);
                listElementsHTML.append(deviceHTML);
                break;
            case "light_controller":
                const intensityValueHTML = document.createElement("div");
                intensityValueHTML.classList.add('light-intensity');
                intensityValueHTML.innerHTML = `${parseInt(deviceValue.intensity)}%`;
                deviceHTML.append(intensityValueHTML);
                deviceHTML.classList.add("light-controller");
                console.log("Switch: " + deviceValue.active);
                const intensityValue = parseFloat(deviceValue.intensity / 100);
                if(deviceValue.active) {
                    deviceHTML.classList.add("active");
                    console.log();
                    deviceHTML.style.backgroundColor = `rgba(${deviceValue.color['red']},
                    ${deviceValue.color['green']},
                    ${deviceValue.color['blue']},
                    ${intensityValue})`;
                } else {
                    deviceHTML.classList.add("no-active");
                }
                deviceImgHTML = new Image();
                deviceImgHTML.src = './img/light-bulb.png';
                deviceHTML.append(deviceImgHTML);
                listElementsHTML.append(deviceHTML);
                break;
            default:
                break;
        }  
         
    }

    locationHTML.append(listElementsHTML);

    const floorHTML = document.createElement("h3");
    floorHTML.classList.add("floor");
    floorHTML.innerHTML = locationValue.floor;   
    locationHTML.append(floorHTML);

    locations.append(locationHTML);
}

function retrieveData() {
    fetch('http://127.0.0.1:7070/api/iot/inventory/location/', {
        method: 'GET',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(listLocations => 
        {
            locationsHTML.innerHTML = "";
            listLocations.forEach(createLocation);
        })
    .catch((error) => {
        console.log("Connection Error");
        const ErrorMessage = "<h2>Sorry but there are connection problems</h2>"
        locationsHTML.innerHTML = ErrorMessage;
    });
}

retrieveData();
//window.setInterval(retrieveData, 500);