package com.treinamento;

import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbJSON;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;

public class AdicionarNumeros_JavaCompute1 extends MbJavaComputeNode {

	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		MbOutputTerminal out = getOutputTerminal("out");
		MbMessage inMessage = inAssembly.getMessage();
		MbMessageAssembly outAssembly = null;
		try {
			// ----------------------------------------------------------
			// Add user code below
			Integer numero1 = (Integer) inMessage.getRootElement().getFirstElementByPath("JSON/Data/numero1").getValue();
			Integer numero2 = (Integer) inMessage.getRootElement().getFirstElementByPath("JSON/Data/numero2").getValue();
			int resultado = numero1 + numero2;
			//Create response message
			MbMessage outMessage = new MbMessage();
			outAssembly = new MbMessageAssembly(inAssembly, outMessage);
			copyMessageHeaders(inMessage, outMessage);
			MbElement outRoot = outMessage.getRootElement();
			MbElement outJsonRoot = outRoot
					.createElementAsLastChild(MbJSON.PARSER_NAME); // Create JSON message
			MbElement data = outJsonRoot.createElementAsLastChild(
					MbElement.TYPE_NAME, MbJSON.DATA_ELEMENT_NAME, null); // Create Data element, must be first after JsonParser
			MbElement reponse = data.createElementAsLastChild(MbJSON.OBJECT, "response", null); // Create a custom response object
			reponse.createElementAsLastChild(MbElement.TYPE_NAME_VALUE, "resultado", resultado); // code 0 = OK message
			// End of user code
			// ----------------------------------------------------------
		} catch (MbException e) {
			// Re-throw to allow Broker handling of MbException
			throw e;
		} catch (RuntimeException e) {
			// Re-throw to allow Broker handling of RuntimeException
			throw e;
		} catch (Exception e) {
			// Consider replacing Exception with type(s) thrown by user code
			// Example handling ensures all exceptions are re-thrown to be handled in the flow
			throw new MbUserException(this, "evaluate()", "", "", e.toString(),
					null);
		}
		// The following should only be changed
		// if not propagating message to the 'out' terminal
		out.propagate(outAssembly);

	}
	
	public void copyMessageHeaders(MbMessage inMessage, MbMessage outMessage)
			throws MbException {
		MbElement outRoot = outMessage.getRootElement();
		MbElement header = inMessage.getRootElement().getFirstChild();

		while (header != null && header.getNextSibling() != null) {
			outRoot.addAsLastChild(header.copy());
			header = header.getNextSibling();
		}
	}

}
