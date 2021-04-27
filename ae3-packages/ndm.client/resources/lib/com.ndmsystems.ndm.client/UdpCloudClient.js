const ae3 = require('ae3');

const UdpCloudService = require('./UdpCloudService');

const UdpCloudClient = module.exports = ae3.Class.create(
	"UdpCloudClient",
	UdpCloudService.RemoteServicePrincipal,
	function UdpCloudClient(client, key, secret, serial){
		this.RemoteServicePrincipal(key, undefined, secret, serial);
		Object.defineProperties(this, {
			client : {
				value : client
			},
			targetSpec : {
				value : 'udp.' + client.ndssHost + ':4044'
			}
		});
		this.registerHandler(UdpCloudService.MsgCall, (function (message, address, serial){
			const component = this.client.components[message.component];
			if(undefined === component || !component.AbstractComponent){
				console.log(">>>>>> ndm.client:UdpCloudService::handlerCall: invalid component: %s", message.component);
				this.sendSingle(new UdpCloudService.MsgCerr(serial, 0x01 /* No Such Component */), address);
				return;
			}
			const args = message.argument.toStringUtf8().split('\n');
			const result = component.prepareCall(args);
			if(!result){
				console.log(">>>>>> ndm.client:UdpCloudService::handlerCall(%s, %s) => CERR: %s", this, message, component.componentName);
				this.sendSingle(new UdpCloudService.MsgCerr(serial, 0x03 /* Invalid Arguments */), address);
				return;
			}
			console.log(">>>>>> ndm.client:UdpCloudService::handlerCall(%s, %s) => CACK, %s", this, message, component.componentName);
			this.sendSingle(new UdpCloudService.MsgCack(serial), address);
			setTimeout(result, 0);
		}).bind(this));
		this.registerHandler(UdpCloudService.MsgRrst, (function (message, address, serial){
			this.sendSingle(new UdpCloudService.MsgCerr(serial, 0x01 /* No Such Component */), address);
			return;
			/**
			 * FIXME: implement, use ru.myx.ae3.state
			 */
			const component = this.client.components[message.component];
			if(undefined === component || !component.AbstractComponent){
				console.log(">>>>>> ndm.client:UdpCloudService::handlerRrst: invalid component: %s", message.component);
				this.sendSingle(new UdpCloudService.MsgCerr(serial, 0x01 /* No Such Component */), address);
				return;
			}
			const args = message.argument.toStringUtf8().split('\n');
			const result = component.prepareCall(args);
			if(!result){
				console.log(">>>>>> ndm.client:UdpCloudService::handlerRrst(%s, %s) => CERR: %s", this, message, component.componentName);
				this.sendSingle(new UdpCloudService.MsgCerr(serial, 0x03 /* Invalid Arguments */), address);
				return;
			}
			console.log(">>>>>> ndm.client:UdpCloudService::handlerRrst(%s, %s) => RSST, %s", this, message, component.componentName);
			this.sendSingle(new UdpCloudService.MsgRsst(/** FIXME: TODO: */ RSST.makeReply(message.rrst), serial), address);
			setTimeout(result, 0);
		}).bind(this));
		return this;
	},
	{
		"localSocketAddress" : {
			value : UdpCloudService.sock.localSocketAddress
		},
		"iface" : {
			value : UdpCloudService
		},
		"sendUdp" : {
			value : UdpCloudService.sendUdp
		},
		"start" : {
			value : function(){
				UdpCloudService.registerClient(this);
				this.RemoteServicePrincipal.prototype.start.call(this);
			}
		},
		"destroy" : {
			value : function(){
				UdpCloudService.removeClient(this);
				this.RemoteServicePrincipal.prototype.destroy.call(this);
			}
		},
		"toString" : {
			value : function(){
				return [ "[UdpCloudClient, ", this.client, ", target=", this.targetSpec, ", ", UdpCloudService, "]" ].join("");
				return "[UdpCloudClient, " + this.client + ", target=" + this.targetSpec + ", " + UdpCloudService + "]";
			}
		}
	}
);


