package translate;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import structured.FunctionSignature;

public class CreateCallasFiles {
	
	private String callasFile;
	private List<FunctionSignature> functionSignatures;
	private FileWriter fr;
	
	private StringBuilder ifaceFile, mainFile, sinkFile, nodeFile, udpFile;
	
	public CreateCallasFiles (StringBuilder callas, List<FunctionSignature> fs, 
			String wsnId,
			String gatewayIp, int gatewayPort, 
			String sinkIp, int sinkPort,
			String broadcastIp, int nodePort)
			throws IOException {
		callasFile = callas.toString();
		functionSignatures = fs;
		defineIface();
		defineMain(wsnId);
		defineSink(wsnId, sinkPort, gatewayIp, gatewayPort, broadcastIp, nodePort);
		defineNode(wsnId, sinkIp, sinkPort, nodePort);
		defineUdp();
		createFiles(wsnId);
	}
	
	private void defineIface() {
		ifaceFile = new StringBuilder();
		ifaceFile.append(
			"\ndefmodule Nil:\n" +
			"   pass\n" +
			"\ndefmodule Sensor:\n" +
			"   Nil init()\n" +
			"   Nil receiver(string c2)\n" +
			"   Nil setVariable_bool(string key, bool value)\n" +   
			"   Nil setVariable_double(string key, double value)\n" +   
			"   Nil setVariable_long(string key, long value)\n" +   
			"   Nil setVariable_string(string key, string value)\n" +   
			"   string gatewayIpPort()\n" +
			"   string sinkIpPort()\n" +
			"   string broadcastIpPort()\n" +
			"   Nil timer_init()\n"
			);
		
		for (FunctionSignature fs : functionSignatures) {
			ifaceFile.append("   Nil " + fs.name + "(");
			
			boolean firstArg = true;
			for (structured.VarDecl arg : fs.arguments) {
				if (firstArg) firstArg = false;
				else ifaceFile.append(", ");
				ifaceFile.append(arg.type.toString() + " " + arg.var.name);
			}
			
			ifaceFile.append(")\n");
		}
	}
	
	private void defineMain(String wsnId) {
		mainFile = new StringBuilder();
		mainFile.append(
			"\ninterface = iface_" + wsnId + ".caltype\n" +
			"\nsensor:\n" +
			"   code = node_" + wsnId + ".callas\n" +
			"\nsensor:\n" +
			"   code = sink_" + wsnId + ".callas\n"
			);
	}
	
	private void defineSink(String wsnId, int sinkPort, String gatewayIp, int gatewayPort, String broadcastIp, int nodePort) {
		sinkFile = new StringBuilder();
		sinkFile.append(
				"\nfrom iface_" + wsnId + " import *\n\n" +
				"module sink of Sensor:\n" +
				"   def init(self):\n" +
				"      c2 = \"" + sinkPort + "\";\n" +
				"      open c2\n" +
				"      extern logString(\"Start\\n\")\n" +
				"      c3 = self.gatewayIpPort();\n" +
			    "      open c3\n" + 
				"      receiver(c2) every 1000\n" +
				"      pass\n\n" +
				"   def receiver(self, c2):\n" +
				"      select c2 receive\n\n" +
				"   def gatewayIpPort(self):\n" +
                "      \"" + gatewayIp + ":" + gatewayPort + "\";\n\n" +
				"   def sinkIpPort(self):\n" +
                "      \"\";\n\n" +
				"   def broadcastIpPort(self):\n" +
                "      \"" + broadcastIp + ":" + nodePort + "\";\n\n" +
				"   def timer_init(self):\n" +
                "      pass\n\n" +
				"   def setVariable_bool(self, key, value):\n" +
                "      extern logString(\"Received variable: \")\n" +
				"      extern logString(key)\n" +
				"      extern logString(\"\\n\")\n" +
			    "      c3 = self.gatewayIpPort()\n" +
			    "      select c3 send setVariable_bool(key, value)\n" +
				"      pass\n\n" +
				"   def setVariable_double(self, key, value):\n" +
                "      extern logString(\"Received variable: \")\n" +
				"      extern logString(key)\n" +
				"      extern logString(\" (\")\n" +
				"      extern logDouble(value)\n" +
				"      extern logString(\")\\n\")\n" +
			    "      c3 = self.gatewayIpPort()\n" +
			    "      select c3 send setVariable_double(key, value)\n" +
				"      pass\n\n" +
				"   def setVariable_long(self, key, value):\n" +
                "      extern logString(\"Received variable: \")\n" +
				"      extern logString(key)\n" +
				"      extern logString(\" (\")\n" +
				"      extern logLong(value)\n" +
				"      extern logString(\")\\n\")\n" +
			    "      c3 = self.gatewayIpPort()\n" +
			    "      select c3 send setVariable_long(key, value)\n" +
				"      pass\n\n" +
				"   def setVariable_string(self, key, value):\n" +
                "      extern logString(\"Received variable: \")\n" +
				"      extern logString(key)\n" +
				"      extern logString(\" (\")\n" +
				"      extern logString(value)\n" +
				"      extern logString(\")\\n\")\n" +
			    "      c3 = self.gatewayIpPort()\n" +
			    "      select c3 send setVariable_string(key, value)\n" +
				"      pass\n\n"
				);
		
		for (FunctionSignature fs : functionSignatures) {
			sinkFile.append("   def " + fs.name + "(self");
			for (structured.VarDecl arg : fs.arguments) sinkFile.append(", " + arg.var.name);
			sinkFile.append(
				"):\n" +
				"      extern logString(\"Calling " + fs.name + " of WSN\\n\")\n" +
				"      c1 = \"" + broadcastIp + ":" + nodePort + "\";\n" +
				"      open c1\n" +
				"      select c1 send " + fs.name + "(");
			
			boolean firstArg = true;
			for (structured.VarDecl arg : fs.arguments) {
				if (firstArg) firstArg = false;
				else sinkFile.append(",");
				sinkFile.append(arg.var.name);
			}
			sinkFile.append(")\n");
			sinkFile.append("      pass\n\n");
		}
	}
	
	private void defineNode(String wsnId, String sinkIp, int sinkPort, int nodePort) {
		nodeFile = new StringBuilder();
		nodeFile.append(
				"\nfrom iface_" + wsnId + " import *\n\n" + 
				callasFile +
				"\n" +
				"   def init(self):\n" +
				"      c1 = self.sinkIpPort();\n" +
				"      c2 = \"" + nodePort + "\";\n" +
				"      open c1\n" +
				"      open c2\n" +
				"      extern logString(\"Start node\\n\")\n" +
				"      receiver(c2) every 1000\n" +
				"      self.timer_init()\n" +
				"      pass\n\n" +
				"   def receiver(self, c2):\n" +
				"      select c2 receive\n\n" +
				"   def gatewayIpPort(self):\n" +
                "      \"\";\n\n" +
				"   def sinkIpPort(self):\n" +
                "      \"" + sinkIp + ":" + sinkPort + "\";\n\n" +
				"   def broadcastIpPort(self):\n" +
                "      \"\";\n\n" +
				"   def setVariable_bool(self, key, value):\n" +
				"      pass\n\n" +
				"   def setVariable_double(self, key, value):\n" +
				"      pass\n\n" +
				"   def setVariable_long(self, key, value):\n" +
				"      pass\n\n" +
				"   def setVariable_string(self, key, value):\n" +
				"      pass\n\n"
				);
	}
	
	private void defineUdp() {
		udpFile = new StringBuilder();
		udpFile.append(
				"defmodule Nil: pass\n" +
				"defmodule Extern:\n" +
				"   bool logLong(long val)\n" +
				"   bool logDouble(double val)\n" +
				"   bool logString(string val)\n" +
				"   string macAddr()\n" +
				"   bool blink()\n" +
				"   bool setLEDColor(long pos, long red, long green, long blue)\n" +
				"   bool setLEDOn(long pos, bool isOn)\n" +
				"   long getLuminosity()\n" +
				"   double getTemperature()\n" +
				"   long getTime()\n" +
				"   double getAccelX()\n" +
				"   double getAccelY()\n" +
				"   double getAccelZ()\n" +
				"   double getAccel()\n" +
				"   double getInclX()\n" +
				"   double getInclY()\n" +
				"   double getInclZ()\n" +
				"   long getBatteryLevel()\n"
				);
	}
	
	private void createFiles(String wsnId) throws IOException {
		//create iface file
		fr = new  FileWriter("iface_" + wsnId + ".caltype");
		fr.write(ifaceFile.toString());
		fr.flush();
		
		//create main file
		fr = new FileWriter("main_" + wsnId + ".calnet");
		fr.write(mainFile.toString());
		fr.flush();
		
		//create sink file
		fr = new FileWriter("sink_" + wsnId + ".callas");
		fr.write(sinkFile.toString());
		fr.flush();
		
		//create node file
		fr = new FileWriter("node_" + wsnId + ".callas");
		fr.write(nodeFile.toString());
		fr.flush();
		
		//create node file
		fr = new FileWriter("udp_" + wsnId + ".caltype");
		fr.write(udpFile.toString());
		fr.flush();
	}
}
