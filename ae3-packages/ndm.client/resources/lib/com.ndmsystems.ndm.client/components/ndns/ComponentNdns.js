const ae3 = require("ae3");

const ComponentNdns = module.exports = ae3.Class.create(
	"ComponentNdns",
	require("./../AbstractComponent"),
	function(client){
		this.AbstractComponent.call(this, client);
		return this;
	},
	{
		componentName : {
			value : "ndns"
		},
		acceptXmlNotifications : {
			value : ['ub1','ut1']
		},
		
		requestXmlNotifications : {
			get : function(){
				if(this.lastUpdated && this.lastUpdated.getDate() + 4 * 60 * 60 * 1000 > Date.now()){
					return null;
				}
				return this.acceptXmlNotifications;
			}
		},
		
		
		lastUpdated : {
			/**
			 * last time data was successfully updated
			 */
			value : null
		},
	},
	{
		newInstance : {
			value : function(client){
				return new ComponentNdns(client);
			}
		}
	}
);