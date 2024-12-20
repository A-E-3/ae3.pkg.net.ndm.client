const ae3 = require('ae3');
const vfs = ae3.vfs;
const Client = require('./Client');


/**
 * Tells whether service is started or stopped
 */
let stopped = true;

/**
 * Client persistent state data location
 */
const clientsFolder = ae3.vfs.ROOT.relativeFolderEnsure("storage/data/ndm.client/clients");

/**
 * Client instances map
 */
const clientObjects = {};


const f = {
	checkClients : function(){
		console.log("ndm.client::NdmCloudService:checkClients: starting...");

		this.udpService || Object.defineProperty(this, "udpService", {
			writable : true,
			value : require('./UdpCloudService')
		});

		const clients = ae3.Util.Settings.SettingsBuilder.builderSimple()//
			.setInputFolderPath("settings/ndm.ndss-client")//
			.setDescriptorReducer(function(settings, description){
				if (description.type !== "ndm.client/Connection") {
					return settings;
				}
				const name = description.name;
				if(!name){
					throw "Client 'name' is expected!";
				}
				const service = description.service;
				const folder = clientsFolder.relativeFolder(name);
				settings[name] = new Client(folder, service.host, service.key, service.pass);
				return settings;
			})//
			.get()
		;

		console.log("ndm.client::NdmCloudService:checkClients: file based configuration loaded, clients: [%s]", Object.keys(clients).join());
		
		for(var folder of clientsFolder.getContentCollection(null).filter(vfs.isContainer)){
			clients[folder.key] ||= new Client(folder);
		}

		console.log("ndm.client::NdmCloudService:checkClients: vfs persistent checked, clients folder: %s, clients: [%s]", clientsFolder, Object.keys(clients).join());

		
		for(var key of Object.keys(clients)){
			if(!clientObjects[key]){
				clientObjects[key] = clients[key];
				clientObjects[key].start();
			}
		}
		for(var key of Object.keys(clientObjects)){
			if(!clients[key]){
				clientObjects[key].destroy();
				delete clientObjects[key];
			}
		}
		console.log("ndm.client::NdmCloudService:checkClients: done. clients: [%s]", Object.keys(clientObjects).join());
	}
};


Object.defineProperties(exports, {
	udpService : {
		writable : true,
		value : null
	},
	description : {
		enumerable : true,
		value : "'ndm.client' service: cloud registration and UDP push notifications support."
	},
	
	
	
	getClient : {
		value : function(key){
			return clientObjects[key];
		}
	},
	getClients : {
		value : function(){
			return Object.assign({}, clientObjects);
		}
	},
	updateClient : {
		value : function(clientId, licenseNumber, serviceKey){
			const clientCurrent = clientObjects[clientId];
			const clientFolder = clientsFolder.relativeFolderEnsure(clientId); // clientsFolder.relativeFolder(clientId);
			if(!Client.storeRaw(clientFolder, clientId, ndssHost, licenseNumber, serviceKey)){
				return false;
			}
			const clientUpdated = new Client(clientFolder);
			if(clientCurrent){
				if(clientCurrent.equals(clientUpdated)){
					return clientCurrent;
				}
				clientCurrent.destroy();
			}
			clientObjects[clientId] = clientUpdated;
			clientUpdated.start();
			return clientUpdated;
		}
	},
	
	
	
	start : {
		value : ae3.Concurrent.wrapSync(function(){
			if(!stopped){
				throw "already started";
			}
			stopped = false;
			
			setTimeout(f.checkClients.bind(this), 500);
		})
	},
	stop : {
		value : ae3.Concurrent.wrapSync(function(){
			if(stopped){
				throw "already stopped";
			}
			stopped = true;
			
			if(this.udpService !== null){
				this.udpService.destroy?.();
				Object.defineProperty(this, "udpService", {
					writable : true,
					value : null
				});
			} 
		})
	},
	toString : {
		value : function(){
			return "[NdmClientService, started: " + (!stopped) + "]";
		}
	}
});
