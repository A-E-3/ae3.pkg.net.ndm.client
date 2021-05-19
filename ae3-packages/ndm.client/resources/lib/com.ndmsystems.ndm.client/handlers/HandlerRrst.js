const UdpCloudService = require('./../UdpCloudService');
const RemoteServiceStateSAPI = require("java.class/ru.myx.ae3.state.RemoteServiceStateSAPI");

/**
 * 
 * 'this' must be bint to an UdpCloudClient instance
 * 
**/
const HandlerRrst = module.exports = function(message, address, serial){
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
};