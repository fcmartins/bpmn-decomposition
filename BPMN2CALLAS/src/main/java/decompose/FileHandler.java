package decompose;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBElement;

import bpmn2.TActivity;
import bpmn2.TAssociation;
import bpmn2.TDataObject;
import bpmn2.TDataStoreReference;
import bpmn2.TFlowElement;
import bpmn2.TLane;
import bpmn2.TLaneSet;
import bpmn2.TProcess;
import bpmn2.TSequenceFlow;

public class FileHandler {

	private List<TProcess> processList;
	private List<AssociationPair<String,TAssociation>> associationPairList;
	private final String filePath;
	private final String dotFileName;
	private int dotFileCounter;
	protected List<List<List<String>>> txtFileInformation;

	protected FileHandler(List<TProcess> processList, List<AssociationPair<String,TAssociation>> associationPairList) {
		this.processList = processList;
		this.associationPairList = associationPairList;

		// D:\pati\software\daniel\bpmn2callas-code\workspace\BPMN2CALLAS\dotFiles\
		filePath = "D:" + File.separator + "pati" + File.separator + "software" + File.separator + "daniel"
				+ File.separator + "bpmn2callas-code" + File.separator + "workspace" + File.separator + "BPMN2CALLAS"
				+ File.separator + "dotFiles" + File.separator;
		dotFileName = "dotFile";
		dotFileCounter = 0;
		txtFileInformation = new ArrayList<>();
	}

	public void deletePreviousDotFiles() {
		File dir = new File(filePath);
		for (File file : dir.listFiles()) {
			if (!file.isDirectory() && !file.delete()){
				System.out.println("No dot files were deleted!");
			}
		}
	}

	private StringBuilder createDataReferences(TActivity activity){

		StringBuilder write = new StringBuilder();

		if (!activity.getDataOutputAssociation().isEmpty()) {
			for (int d = 0; d < activity.getDataOutputAssociation().size(); d++) {
				if (activity.getDataOutputAssociation().get(d).getTargetRef() instanceof TDataObject) {

					TDataObject taskDataObject = (TDataObject) activity
							.getDataOutputAssociation().get(d).getTargetRef();

					TSequenceFlow taskDataSequenceFlow = new TSequenceFlow();
					taskDataSequenceFlow.setSourceRef(activity);
					taskDataSequenceFlow.setTargetRef(taskDataObject);
					write.append(FlowElementUtil.printFlowElementName(new ArrayList<Object>(
							Arrays.asList(taskDataSequenceFlow.getSourceRef(),
									taskDataSequenceFlow,
									taskDataSequenceFlow.getTargetRef()))));

				} else if (activity.getDataOutputAssociation().get(d).getTargetRef() instanceof TDataStoreReference) {

					TDataStoreReference dataStoreReference = (TDataStoreReference) activity
							.getDataOutputAssociation().get(d).getTargetRef();

					TSequenceFlow taskDataStoreSequenceFlow = new TSequenceFlow();
					taskDataStoreSequenceFlow.setSourceRef(activity);
					taskDataStoreSequenceFlow.setTargetRef(dataStoreReference);
					write.append(FlowElementUtil.printFlowElementName(new ArrayList<Object>(
							Arrays.asList(taskDataStoreSequenceFlow.getSourceRef(),
									taskDataStoreSequenceFlow,
									taskDataStoreSequenceFlow.getTargetRef()))));
				} else if (activity.getDataOutputAssociation().get(d).getSourceRef() instanceof ArrayList) {

					List<JAXBElement<Object>> jaxbElementList = activity.getDataOutputAssociation().get(d).getSourceRef();

					for (int j = 0; j < jaxbElementList.size(); j++) {
						TSequenceFlow taskDataAssociationSequenceFlow = new TSequenceFlow();
						taskDataAssociationSequenceFlow.setSourceRef(jaxbElementList.get(j)
								.getValue());
						taskDataAssociationSequenceFlow.setTargetRef(activity);

						write.append(FlowElementUtil
								.printFlowElementName(new ArrayList<Object>(Arrays.asList(
										taskDataAssociationSequenceFlow.getSourceRef(),
										taskDataAssociationSequenceFlow,
										taskDataAssociationSequenceFlow.getTargetRef()))));
					}

				} else if (activity.getDataOutputAssociation().get(d).getTargetRef() instanceof ArrayList) {

					List<JAXBElement<Object>> jaxbElementList = (List<JAXBElement<Object>>) activity
							.getDataOutputAssociation().get(d).getTargetRef();

					for (int j = 0; j < jaxbElementList.size(); j++) {
						TSequenceFlow taskDataAssociationSequenceFlow = new TSequenceFlow();
						taskDataAssociationSequenceFlow.setSourceRef(activity);
						taskDataAssociationSequenceFlow.setTargetRef(jaxbElementList.get(j)
								.getValue());

						write.append(FlowElementUtil
								.printFlowElementName(new ArrayList<Object>(Arrays.asList(
										taskDataAssociationSequenceFlow.getSourceRef(),
										taskDataAssociationSequenceFlow,
										taskDataAssociationSequenceFlow.getTargetRef()))));
					}
				}
			}
		}
		
		if (!activity.getDataInputAssociation().isEmpty()) {
			for (int d = 0; d < activity.getDataInputAssociation().size(); d++) {

				if (activity.getDataInputAssociation().get(d).getTargetRef() instanceof TDataObject) {

					TDataObject taskDataObject = (TDataObject) activity
							.getDataInputAssociation().get(d).getSourceRef().get(0)
							.getValue();

					TSequenceFlow taskDataSequenceFlow = new TSequenceFlow();
					taskDataSequenceFlow.setSourceRef(taskDataObject);
					taskDataSequenceFlow.setTargetRef(activity);
					write.append(FlowElementUtil.printFlowElementName(new ArrayList<Object>(
							Arrays.asList(taskDataSequenceFlow.getSourceRef(),
									taskDataSequenceFlow,
									taskDataSequenceFlow.getTargetRef()))));
					
				} else if (activity.getDataInputAssociation().get(d).getTargetRef() instanceof TDataStoreReference) {

					TDataStoreReference dataStoreReference = (TDataStoreReference) activity
							.getDataInputAssociation().get(d).getSourceRef().get(0)
							.getValue();

					TSequenceFlow taskDataStoreSequenceFlow = new TSequenceFlow();
					taskDataStoreSequenceFlow.setSourceRef(dataStoreReference);
					taskDataStoreSequenceFlow.setTargetRef(activity);
					write.append(FlowElementUtil.printFlowElementName(new ArrayList<Object>(
							Arrays.asList(taskDataStoreSequenceFlow.getSourceRef(),
									taskDataStoreSequenceFlow,
									taskDataStoreSequenceFlow.getTargetRef()))));
					
				} else if (activity.getDataInputAssociation().get(d).getSourceRef() instanceof ArrayList) {

					List<JAXBElement<Object>> jaxbElementList = activity
							.getDataInputAssociation().get(d).getSourceRef();

					for (int j = 0; j < jaxbElementList.size(); j++) {
						TSequenceFlow taskDataAssociationSequenceFlow = new TSequenceFlow();
						taskDataAssociationSequenceFlow.setSourceRef(jaxbElementList.get(j)
								.getValue());
						taskDataAssociationSequenceFlow.setTargetRef(activity);

						write.append(FlowElementUtil
								.printFlowElementName(new ArrayList<Object>(Arrays.asList(
										taskDataAssociationSequenceFlow.getSourceRef(),
										taskDataAssociationSequenceFlow,
										taskDataAssociationSequenceFlow.getTargetRef()))));
					}

				} else if (activity.getDataInputAssociation().get(d).getTargetRef() instanceof ArrayList) {

					List<JAXBElement<Object>> jaxbElementList = (ArrayList<JAXBElement<Object>>) activity
							.getDataInputAssociation().get(d).getTargetRef();

					for (int j = 0; j < jaxbElementList.size(); j++) {
						TSequenceFlow taskDataAssociationSequenceFlow = new TSequenceFlow();
						taskDataAssociationSequenceFlow.setSourceRef(activity);
						taskDataAssociationSequenceFlow.setTargetRef(jaxbElementList.get(j)
								.getValue());

						write.append(FlowElementUtil
								.printFlowElementName(new ArrayList<Object>(Arrays.asList(
										taskDataAssociationSequenceFlow.getSourceRef(),
										taskDataAssociationSequenceFlow,
										taskDataAssociationSequenceFlow.getTargetRef()))));
					}
				}
			}
		}
		
		return write;
		
	}
	
	public void createDotFiles() throws IOException{

		for (TProcess process : processList) {
			dotFileCounter++;
			File file = new File(filePath + dotFileName + dotFileCounter + ".dot");
			file.getParentFile().mkdirs();

			if (!file.exists()) {
				try {
					if(!file.createNewFile()){
						System.out.println("Dot file "+dotFileName+dotFileCounter+" was not created!");
					}
				} catch (IOException e) {
					System.err.println("An error has occured creating a dot file.");
					throw new RuntimeException(e);
				}
			}
			/*
			 * Write the data to the files
			 */
			
			FileWriter fw = null;
			
			try {
				fw = new FileWriter(file);
				StringBuilder write = new StringBuilder("digraph G {\nrankdir=LR;\nsplines=false;\n");

				if (!process.getLaneSet().isEmpty()) {

					List<String> clusterList = new ArrayList<>();
					List<String> outsideClusterList = new ArrayList<>();
					write.append("newrank=true;\n");

					for (TLaneSet laneSet : process.getLaneSet()) {

						int clusterCounter = 1;

						for (TLane lane : laneSet.getLane()) {

							StringBuilder cluster = new StringBuilder();
							cluster.append("subgraph cluster_" + clusterCounter + "{\n");

							for (int i = 0; i < process.getFlowElement().size(); i++) {

								TFlowElement flowElement = process.getFlowElement().get(i).getValue();

								if (!(flowElement instanceof TSequenceFlow) && lane.getId()
											.equals(LaneUtil.getFlowElementLaneId(process, flowElement.getId()))) {
										
										List<TSequenceFlow> sequenceFlowList = FlowElementUtil
												.getSequenceFlowFromFlowElementSource(processList, flowElement);

										/*
										 * sequenceFlow is null when flowElement
										 * is an endEvent. This avoids duplicate
										 * entries on the dot file
										 */
										if (!sequenceFlowList.isEmpty()) {
											for (TSequenceFlow sequenceFlow : sequenceFlowList) {
												TFlowElement target = FlowElementUtil
														.getFlowElementFromSequenceFlowReference(processList,
																sequenceFlow.getTargetRef());

												if (LaneUtil.getFlowElementLaneIndex(process, flowElement.getId()) == LaneUtil
														.getFlowElementLaneIndex(process, target.getId())) {
													cluster.append(FlowElementUtil
															.printFlowElementName(new ArrayList<Object>(Arrays.asList(
																	sequenceFlow.getSourceRef(), sequenceFlow,
																	sequenceFlow.getTargetRef()))));
												} else {
													outsideClusterList.add(flowElement.getId() + " -> "
															+ target.getId());
												}
											}
										}
										
										/*
										 * Add associations to the dot file if they exist
										 */
										for (int a = 0; a < associationPairList.size(); a++) {
											if(associationPairList.get(a).getProcessName().equals(process.getName())){
												
												TAssociation association = associationPairList.get(a).getAssociation();
												
												if(flowElement.getId().equals(association.getSourceRef().getLocalPart()) ||
													flowElement.getId().equals(association.getTargetRef().getLocalPart())){
												
													if(!cluster.toString().contains(association.getSourceRef().getLocalPart() + " -> "+ association.getTargetRef().getLocalPart())){
														cluster.append("\t" + association.getSourceRef().getLocalPart() + " -> "
															+ association.getTargetRef().getLocalPart() + ";\n");
													}
													
													break;
												}
											}
										}
										
										/*
										 * Create data object and data store references for lanes
										 */
										
										if(FlowElementUtil.isTask(flowElement)){
											cluster.append(createDataReferences((TActivity) flowElement));
										}
										
										
									}	

							}
							cluster.append("}\n");
							clusterList.add(cluster.toString());
							clusterCounter++;
						}
					}

					/*
					 * Adds the cluster info to the dot file
					 */
					for (String cluster : clusterList) {
						write.append(cluster);
					}

					/*
					 * Centers the clusters
					 */
					List<String> centerList = new ArrayList<>(clusterList.size());
					for (String cluster : clusterList) {
						String[] stringSplit = cluster.split("\\s");
						/*
						 * This position corresponds to the first flow element in the
						 * cluster
						 */
						centerList.add(stringSplit[3]);
						/*
						 * We want all start events aligned
						 */
						for (int i = 4; i < stringSplit.length; i++) {
							if (stringSplit[i].contains("StartEvent")) {
								centerList.add(stringSplit[i]);
							}
						}

					}

					if (centerList.size() > 1) {
						write.append("{ rank=same; ");
						for (String activity : centerList) {
							write.append(activity + "; ");
						}
						write.append("}\n");
					}

					/*
					 * Adds the connections that occur between clusters
					 */
					for (String outCluster : outsideClusterList) {
						write.append(outCluster + ";\n");
					}

				} else {

					/*
					 * Add associations to the dot file if they exist
					 */
					for (int a = 0; a < associationPairList.size(); a++) {
						if(associationPairList.get(a).getProcessName().equals(process.getName())){
							
							TAssociation association = associationPairList.get(a).getAssociation();
							
							write.append("\t" + association.getSourceRef().getLocalPart() + " -> "
									+ association.getTargetRef().getLocalPart() + ";\n");

						}
					}
					
					for (int i = 0; i < process.getFlowElement().size(); i++) {

						TFlowElement flowElement = process.getFlowElement().get(i).getValue();

						if (flowElement instanceof TSequenceFlow) {
							TSequenceFlow sequenceFlow = (TSequenceFlow) flowElement;
							write.append(FlowElementUtil.printFlowElementName(new ArrayList<Object>(Arrays.asList(
									sequenceFlow.getSourceRef(), sequenceFlow, sequenceFlow.getTargetRef()))));
							/*
							 * Add data references to the dot file if they exist
							 */
						} else if(FlowElementUtil.isDataTask(flowElement)) {
							write.append(createDataReferences((TActivity) flowElement));
						}
					}
				}
				write.append("}");
				fw.write(write.toString());

			}finally{
				fw.close();
			}
		}
	}

	private void runDotFile(String command) throws IOException {
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd " + filePath + " && " + command);
		builder.redirectErrorStream(true);
		Process p = builder.start();
		// Writes error information on the console if needed
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while (true) {
			line = r.readLine();
			if (line == null) {
				break;
			}
			System.err.println(line);
		}
	}

	/*
	 * Plain text statements: graph scale width height node name x y width
	 * height label style shape color fillcolor edge tail head n x1 y1 .. xn yn
	 * [label xl yl] style color stop
	 */

	private List<List<String>> getDotStringInformation(String plainText) {

		List<List<String>> result = new ArrayList<>();
		// gets all strings separated by a blank space
		String[] words = plainText.split("\\s+");
		// 14 is the max plain text string size in a line
		List<String> toAdd = new ArrayList<>(14);

		for (int i = 0; i < words.length; i++) {
			if ("node".equals(words[i]) || "edge".equals(words[i]) || "stop".equals(words[i])) {
				result.add(toAdd);
				toAdd = new ArrayList<>(14);
				toAdd.add(words[i]);
			} else {
				toAdd.add(words[i]);

			}
		}
		return result;
	}

	protected void runFiles() {
		/*
		 * Executes the given cmd command in the directory of the dot files
		 */
		try {
			for (int i = 1; i <= dotFileCounter; i++) {
				runDotFile("dot -Tplain " + dotFileName + i + ".dot" + " > " + dotFileName + i + ".txt");
				runDotFile("dot -Tpng " + dotFileName + i + ".dot" + " > " + dotFileName + i + ".png");

				String txtFileInfo = new String(Files.readAllBytes(Paths.get(filePath + dotFileName + i + ".txt")));
				List<List<String>> toAdd = getDotStringInformation(txtFileInfo);
				txtFileInformation.add(toAdd);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected List<List<List<String>>> getTxtFileInformation() {
		return txtFileInformation;
	}

}