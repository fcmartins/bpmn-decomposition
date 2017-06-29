package decompose;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import bpmn2.BPMNEdge;
import bpmn2.BPMNLabel;
import bpmn2.BPMNShape;
import bpmn2.Bounds;
import bpmn2.DiagramElement;
import bpmn2.Point;
import bpmn2.TActivity;
import bpmn2.TAssociation;
import bpmn2.TCollaboration;
import bpmn2.TDataObject;
import bpmn2.TDataStore;
import bpmn2.TDataStoreReference;
import bpmn2.TFlowElement;
import bpmn2.TLane;
import bpmn2.TLaneSet;
import bpmn2.TMessageFlow;
import bpmn2.TParticipant;
import bpmn2.TProcess;
import bpmn2.TSequenceFlow;

public class DrawHandler {

	private TCollaboration collaboration;
	private List<TProcess> newProcessList;
	private List<AssociationPair<String, TAssociation>> associationPairList;
	private FileHandler fh;
	private List<BPMNShape> participantShapeList;
	private List<JAXBElement<? extends DiagramElement>> newDiagramElement;
	/*
	 * List with all the txt files with a list that contains all the lines in
	 * the given txt file with a list that contains all words on a given line
	 */
	private List<List<List<String>>> txtFileInformation;
	private Map<String, BPMNShape> elementShapeMap;
	// Map<laneId,BPMNShape>
	private Map<String, BPMNShape> laneShapeMap;

	private static final  String namespaceURIDiagram = "http://www.omg.org/spec/BPMN/20100524/DI";
	private static final  String bpmnShapeId = "BPMNShape_";
	private static final String bpmnShapeLaneId = "BPMNShape_Lane_";
	private static final String bpmnEdgeId = "BPMNEdge_";
	private int bpmnShapeIdCounter;
	private int bpmnShapeLaneIdCounter;
	// Map<laneId,[minY,maxY]
	private Map<String, Double[]> laneMinMaxYMap;

	private static final double widthPoolGraphScale = 82;
	private static final double heightPoolGraphScale = 80;
	private static final double elementScale = 80;
	private static final double dataScaleWidth = 20;
	private static final double dataScaleHeight = elementScale;
	private static final double centerSequenceFlows = 21;
	private static final int xLaneDistanceFromPoolName = 30;
	private static final int yPoolDistanceFromOtherPool = 50;

	private static final double extraHeightPoolLane = 1.2;
	private static final double extraYShapes = extraHeightPoolLane / 2;

	private static final double extraWidthPoolLane = 75;

	private double[] yDistanceBetweenPools;
	private List<Double> yDistanceBetweenLanes;
	private double xBounds;
	private boolean dataObjectExist;
	private boolean processListHasLanes;

	protected DrawHandler(TCollaboration collaboration, List<TProcess> newProcessList,
			List<AssociationPair<String, TAssociation>> associationPairList) {
		this.collaboration = collaboration;
		this.newProcessList = newProcessList;
		this.associationPairList = associationPairList;
		fh = new FileHandler(newProcessList, associationPairList);
		participantShapeList = new ArrayList<>();
		newDiagramElement = new ArrayList<>();
		elementShapeMap = new HashMap<>();
		laneShapeMap = new HashMap<>();
		bpmnShapeIdCounter = 1;
		bpmnShapeLaneIdCounter = 1;
		yDistanceBetweenPools = new double[collaboration.getParticipant().size()];
		yDistanceBetweenLanes = new ArrayList<>();
		yDistanceBetweenLanes.add(0.0);
		xBounds = 100;
		dataObjectExist = false;
		processListHasLanes = false;
		laneMinMaxYMap = new HashMap<>();
	}

	/*
	 * Get the distance between the bottommost element and the black border
	 */
	private double getDistanceToClusterBorder() {
		double result = Double.MAX_VALUE;
		double elementHeight = 0.0;

		for (TProcess process : newProcessList) {
			if (!process.getLaneSet().isEmpty()) {
				for (TLaneSet laneSet : process.getLaneSet()) {
					for (TLane lane : laneSet.getLane()) {

						for (int i = 0; i < lane.getFlowNodeRef().size(); i++) {

							Object flowElementId = lane.getFlowNodeRef().get(i).getValue();

							for (List<List<String>> currentFile : txtFileInformation) {
								for (List<String> currentLine : currentFile) {

									if (currentLine.get(0).equals("node") && currentLine.get(1).equals(flowElementId)) {

										double dotY = Double.parseDouble(currentLine.get(3));
										if (dotY < result) {
											System.out.println("Distance to Cluster border element chosen = "
													+ currentLine.get(1));
											result = dotY;
											elementHeight = Double.parseDouble(currentLine.get(5));
										}
									}

									if (currentLine.get(0).equals("edge")) {
										if (currentLine.get(1).equals(flowElementId)) {
											if (currentLine.get(2).contains("DataObject")
													|| currentLine.get(2).contains("DataStore")) {

												for (List<String> currentLineAgain : currentFile) {
													if (currentLineAgain.get(0).equals("node")
															&& currentLineAgain.get(1).equals(currentLine.get(2))) {
														double dotY = Double.parseDouble(currentLineAgain.get(3));

														if (dotY < result) {
															System.out
																	.println("Distance to Cluster border element chosen = "
																			+ currentLineAgain.get(1));
															result = dotY;
															elementHeight = Double.parseDouble(currentLineAgain.get(5));
														}
													}
												}
											}
										} else if (currentLine.get(2).equals(flowElementId)) {
											if (currentLine.get(1).contains("DataObject")
													|| currentLine.get(1).contains("DataStore")) {

												for (List<String> currentLineAgain : currentFile) {
													if (currentLineAgain.get(0).equals("node")
															&& currentLineAgain.get(1).equals(currentLine.get(1))) {
														double dotY = Double.parseDouble(currentLineAgain.get(3));

														if (dotY < result) {
															System.out
																	.println("Distance to Cluster border element chosen = "
																			+ currentLineAgain.get(1));
															result = dotY;
															elementHeight = Double.parseDouble(currentLineAgain.get(5));
														}
													}
												}
											}
										}
									}

								}
							}
						}
					}
				}
			}
		}
		System.out.println("Y = " + result);
		System.out.println("Height = " + elementHeight);
		elementHeight /= 2;
		System.out.println("Height = " + elementHeight);

		return result - elementHeight;
	}

	/*
	 * Checks if graphviz inverted the lane order
	 */
	private boolean checkInvertedLanes(TProcess process, String firstLaneId, String secondLaneId){
		
		double firstY = 0.0; 
		double secondY = 0.0;
		
		for (List<List<String>> currentFile : txtFileInformation) {
			for (List<String> currentLine : currentFile) {
				if (currentLine.get(0).equals("node")) {
					String laneId = LaneUtil.getFlowElementLaneId(process, currentLine.get(1));
					if(laneId != null){
						if(laneId.equals(firstLaneId)){
							firstY = Double.parseDouble(currentLine.get(3));
						}else if(laneId.equalsIgnoreCase(secondLaneId)){
							secondY = Double.parseDouble(currentLine.get(3));
						}
					}
					if(firstY != 0.0 && secondY != 0.0){
						return firstY > secondY;
					}
				}
			}
		}

		return false;
	}
	
	
	/*
	 * Gets the distance between the first two lanes
	 */
	private double getDistanceBetweenClusterLanes() {
		double elementHeight = 0.0;
		double smallY = Double.MAX_VALUE;
		double bigY = Double.MIN_VALUE;

		for (TProcess process : newProcessList) {
			if (!process.getLaneSet().isEmpty()) {		
				
				boolean invertedLanes = checkInvertedLanes(process, process.getLaneSet().get(0).getLane().get(0).getId(), process.getLaneSet().get(0).getLane().get(1).getId());
				
				for (TLaneSet laneSet : process.getLaneSet()) {
					for (int l = 0; l < 2; l++) {

						TLane lane = laneSet.getLane().get(l);
						
						for (int i = 0; i < lane.getFlowNodeRef().size(); i++) {

							Object flowElementId = lane.getFlowNodeRef().get(i).getValue();

							for (List<List<String>> currentFile : txtFileInformation) {
								for (List<String> currentLine : currentFile) {
									if (currentLine.get(0).equals("node") && currentLine.get(1).equals(flowElementId)) {

										double dotY = Double.parseDouble(currentLine.get(3));
		
										if(invertedLanes){
											if (l == 0 && dotY < smallY) {
												System.out.println("OLD SMALLY = "+smallY+"NEW SMALL = "+currentLine.get(1)+ " VALUE = "+dotY);
												smallY = dotY;
												elementHeight = Double.parseDouble(currentLine.get(5));
											}
	
											if (l == 1 && dotY > bigY) {
												System.out.println("OLD BIGY = "+bigY+"NEW BIG = "+currentLine.get(1)+ " VALUE = "+dotY);
												bigY = dotY;
											}
										}else{
											if (l == 0 && dotY > bigY) {
												System.out.println("OLD BIGY = "+bigY+"NEW BIG = "+currentLine.get(1)+ " VALUE = "+dotY);
												bigY = dotY;
												elementHeight = Double.parseDouble(currentLine.get(5));
											}
	
											if (l == 1 && dotY < smallY) {
												System.out.println("OLD SMALLY = "+smallY+"NEW SMALL = "+currentLine.get(1)+ " VALUE = "+dotY);
												smallY = dotY;
											}
										}
									}
									
									
									if(currentLine.get(0).equals("edge")){
										if(currentLine.get(1).equals(flowElementId)){
											if(currentLine.get(2).contains("DataObject") || currentLine.get(2).contains("DataStore")){
												
												for (List<String> currentLineAgain : currentFile) {
													if(currentLineAgain.get(0).equals("node") && currentLineAgain.get(1).equals(currentLine.get(2))){
														double dotY = Double.parseDouble(currentLineAgain.get(3));
														
														if(invertedLanes){
															if (l == 0 && dotY < smallY) {
																System.out.println("OLD SMALLY = "+smallY+"NEW SMALL = "+currentLineAgain.get(1)+ " VALUE = "+dotY);
																smallY = dotY;
																elementHeight = Double.parseDouble(currentLineAgain.get(5));
															}
					
															if (l == 1 && dotY > bigY) {
																System.out.println("OLD BIGY = "+bigY+"NEW BIG = "+currentLineAgain.get(1)+ " VALUE = "+dotY);
																bigY = dotY;
															}
														}else{
															if (l == 0 && dotY > bigY) {
																System.out.println("OLD BIGY = "+bigY+"NEW BIG = "+currentLineAgain.get(1)+ " VALUE = "+dotY);
																bigY = dotY;
																elementHeight = Double.parseDouble(currentLineAgain.get(5));
															}
					
															if (l == 1 && dotY < smallY) {
																System.out.println("OLD SMALLY = "+smallY+"NEW SMALL = "+currentLineAgain.get(1)+ " VALUE = "+dotY);
																smallY = dotY;
															}
														}
													}
												}
											}
										}else if(currentLine.get(2).equals(flowElementId)){
											if(currentLine.get(1).contains("DataObject") || currentLine.get(1).contains("DataStore")){
												
												for (List<String> currentLineAgain : currentFile) {
													if(currentLineAgain.get(0).equals("node") && currentLineAgain.get(1).equals(currentLine.get(1))){
														double dotY = Double.parseDouble(currentLineAgain.get(3));
														
														if(invertedLanes){
															if (l == 0 && dotY < smallY) {
																System.out.println("OLD SMALLY = "+smallY+"NEW SMALL = "+currentLineAgain.get(1)+ " VALUE = "+dotY);
																smallY = dotY;
																elementHeight = Double.parseDouble(currentLineAgain.get(5));
															}
					
															if (l == 1 && dotY > bigY) {
																System.out.println("OLD BIGY = "+bigY+"NEW BIG = "+currentLineAgain.get(1)+ " VALUE = "+dotY);
																bigY = dotY;
															}
														}else{
															if (l == 0 && dotY > bigY) {
																System.out.println("OLD BIGY = "+bigY+"NEW BIG = "+currentLineAgain.get(1)+ " VALUE = "+dotY);
																bigY = dotY;
																elementHeight = Double.parseDouble(currentLineAgain.get(5));
															}
					
															if (l == 1 && dotY < smallY) {
																System.out.println("OLD SMALLY = "+smallY+"NEW SMALL = "+currentLineAgain.get(1)+ " VALUE = "+dotY);
																smallY = dotY;
															}
														}
													}
												}
											}
										}
									}			
									
								}
							}
						}
					}
				}
			}
		}

		double result = 0.0;
				
		System.out.println("BIGY = "+bigY);
		System.out.println("SMALLY = "+smallY);
		
		/*
		 * Depends if the lanes are inverted or not
		 */
		if(bigY > smallY){
			result = bigY - smallY;
		}else{
			result = smallY - bigY;
		}

		System.out.println("RESULT DIFFERENCE = "+result);
		result /= 2;
		result -= elementHeight / 2;
		return result;
	}

	/*
	 * Finds every flowElement in every lane and checks the dot file for the
	 * corresponding min Y and max Y value on the corresponding lane
	 */
	private void initializeLaneYMinMaxMap(double distanceToClusterBorders, double distanceBetweenClusterLanes) {
		for (TProcess process : newProcessList) {
			if (!process.getLaneSet().isEmpty()) {
				for (TLaneSet laneSet : process.getLaneSet()) {

					System.out.println("-----------------------------");

					for (int l = 0; l < laneSet.getLane().size(); l++) {

						TLane lane = laneSet.getLane().get(l);

						double minY = Double.MAX_VALUE;
						double minHeight = 0.0;
						double maxY = Double.MIN_VALUE;
						double maxHeight = 0.0;

						for (int i = 0; i < lane.getFlowNodeRef().size(); i++) {

							Object flowElementId = lane.getFlowNodeRef().get(i).getValue();

							for (List<List<String>> currentFile : txtFileInformation) {
								for (List<String> currentLine : currentFile) {
									if (currentLine.get(0).equals("node") && currentLine.get(1).equals(flowElementId)) {

										double dotY = Double.parseDouble(currentLine.get(3));

										if (dotY > maxY) {
											System.out.println("NEW MAX = " + currentLine.get(1));
											maxY = dotY;
											maxHeight = Double.parseDouble(currentLine.get(5)) / 2;

										} else if (dotY < minY) {
											System.out.println("NEW MIN = " + currentLine.get(1));
											minY = dotY;
											minHeight = Double.parseDouble(currentLine.get(5)) / 2;
										}

									}

									if (currentLine.get(0).equals("edge")) {
										if (currentLine.get(1).equals(flowElementId)) {
											if (currentLine.get(2).contains("DataObject")
													|| currentLine.get(2).contains("DataStore")) {

												for (List<String> currentLineAgain : currentFile) {
													if (currentLineAgain.get(0).equals("node")
															&& currentLineAgain.get(1).equals(currentLine.get(2))) {
														double dotY = Double.parseDouble(currentLineAgain.get(3));

														if (dotY > maxY) {
															System.out.println("NEW MAX = " + currentLineAgain.get(1));
															maxY = dotY;
															maxHeight = Double.parseDouble(currentLineAgain.get(5)) / 2;

														} else if (dotY < minY) {
															System.out.println("NEW MIN = " + currentLineAgain.get(1));
															minY = dotY;
															minHeight = Double.parseDouble(currentLineAgain.get(5)) / 2;
														}
													}
												}
											}
										} else if (currentLine.get(2).equals(flowElementId)) {
											if (currentLine.get(1).contains("DataObject")
													|| currentLine.get(1).contains("DataStore")) {

												for (List<String> currentLineAgain : currentFile) {
													if (currentLineAgain.get(0).equals("node")
															&& currentLineAgain.get(1).equals(currentLine.get(1))) {
														double dotY = Double.parseDouble(currentLineAgain.get(3));

														if (dotY > maxY) {
															System.out.println("NEW MAX = " + currentLineAgain.get(1));
															maxY = dotY;
															maxHeight = Double.parseDouble(currentLineAgain.get(5)) / 2;

														} else if (dotY < minY) {
															System.out.println("NEW MIN = " + currentLineAgain.get(1));
															minY = dotY;
															minHeight = Double.parseDouble(currentLineAgain.get(5)) / 2;
														}
													}
												}
											}
										}
									}
								}
							}
						}

						maxY += distanceBetweenClusterLanes;
						maxY += maxHeight;

						minY -= minHeight;
						minY -= distanceToClusterBorders;

						laneMinMaxYMap.put(lane.getId(), new Double[] { minY, maxY });

						if (l < laneSet.getLane().size() - 1) {
							System.out.println("---------- Next Lane --------");
						} else {
							System.out.println("-----------------------------");
						}
					}
				}
			}
		}
	}

	/*
	 * Checks if lanes exist in any process
	 */
	private boolean processListHasLanes() {

		for (TProcess process : newProcessList) {
			if (!process.getLaneSet().isEmpty()) {
				return true;
			}
		}

		return false;
	}

	/*
	 * Creates and executes all the necessary files that are needed to begin the
	 * draw process
	 */
	private void initiateDrawProcess() {
		fh.deletePreviousDotFiles();
		try {
			fh.createDotFiles();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		fh.runFiles();
		txtFileInformation = fh.getTxtFileInformation();
		processListHasLanes = processListHasLanes();

		if (processListHasLanes) {

			double distanceToClusterBorders = getDistanceToClusterBorder();
			System.out.println("Distance to borders = " + distanceToClusterBorders);

			// 0.1667
			double distanceBetweenClusterLanes = getDistanceBetweenClusterLanes();
			System.out.println("Distance between cluster lanes = " + distanceBetweenClusterLanes);

			initializeLaneYMinMaxMap(distanceToClusterBorders, distanceBetweenClusterLanes);
		}
	}

	/*
	 * Draws the BPMN model pools
	 */
	private void drawPools() {

		int currentPool = 0;

		for (TParticipant participant : collaboration.getParticipant()) {

			double width = Double.parseDouble(txtFileInformation.get(currentPool).get(0).get(2));
			double height = Double.parseDouble(txtFileInformation.get(currentPool).get(0).get(3));

			for (TProcess process : newProcessList) {

				if (process.getId().equals(participant.getProcessRef().getLocalPart())
						&& process.getLaneSet().isEmpty()) {
					height += extraHeightPoolLane;
					break;
				}
			}

			// height += extraHeightPoolLane;

			BPMNShape participantShape = new BPMNShape();
			participantShape.setBpmnElement(new QName(participant.getId()));
			participantShape.setId(bpmnShapeId + bpmnShapeIdCounter);
			bpmnShapeIdCounter++;

			participantShape.setBounds(new Bounds());
			participantShape.setIsHorizontal(true);
			participantShape.getBounds().setX(xBounds);
			participantShape.getBounds().setY(yDistanceBetweenPools[currentPool]);
			participantShape.getBounds().setWidth(width * widthPoolGraphScale + extraWidthPoolLane);
			participantShape.getBounds().setHeight(height * heightPoolGraphScale);

			System.out.println("POOL HEIGHT = " + height * heightPoolGraphScale);

			participantShape.setBPMNLabel(new BPMNLabel());
			participantShape.getBPMNLabel().setBounds(new Bounds());
			participantShapeList.add(participantShape);

			newDiagramElement.add(new JAXBElement<BPMNShape>(new QName(namespaceURIDiagram, "BPMNShape"),
					BPMNShape.class, participantShape));

			currentPool++;
			if (currentPool < yDistanceBetweenPools.length) {
				yDistanceBetweenPools[currentPool] = height * heightPoolGraphScale + yPoolDistanceFromOtherPool
						+ yDistanceBetweenPools[currentPool - 1];
			}
		}
	}

	

	/*
	 * Returns the Y coordinate of the first element it finds from a given lane in a given process
	 */
	private Double getYCoordinateFromALane(TProcess process, String laneIdtoGet){
		for (List<List<String>> currentFile : txtFileInformation) {
			for (List<String> currentLine : currentFile) {
				if (currentLine.get(0).equals("node")) {
					String laneId = LaneUtil.getFlowElementLaneId(process, currentLine.get(1));
					if(laneId != null && laneId.equals(laneIdtoGet)){
						return Double.parseDouble(currentLine.get(3));
					}
				}
			}
		}
		return -1.0;
	}
	
	/*
	 * Returns the lane id from the dot txt file from a given Y coordinate
	 */
	private String getLaneIdFromYCoordinate(TProcess process, Double[] lanesOrderedByY, int currentLane){
		for (List<List<String>> currentFile : txtFileInformation) {
			for (List<String> currentLine : currentFile) {
				if (currentLine.get(0).equals("node")) {
					String laneId = LaneUtil.getFlowElementLaneId(process, currentLine.get(1));
					if(laneId != null){
						Double y = Double.parseDouble(currentLine.get(3));
						if(lanesOrderedByY[currentLane].equals(y)){
							return laneId;
						}
					}
				}
			}
		}
		return null;
	}
	
	/*
	 * Returns a list with lane ids in the correct order stated by the dot txt files
	 */
	private List<String> getOrderedLanes(TProcess process, Double[] lanesOrderedByY){
		List<String> orderedLanes = new ArrayList<>();
	
		for(int i = 0; i < lanesOrderedByY.length; i++){
			orderedLanes.add(getLaneIdFromYCoordinate(process,lanesOrderedByY, i));
		}
		
		return orderedLanes;
	}
	
	/*
	 * Reorders the drawn lanes on the BPMN model to match the order stated by the dot txt file 
	 */
	private void reorderLanes(TProcess process){

		Double[] lanesOrderedByY = new Double[process.getLaneSet().get(0).getLane().size()];
		
		for(int i = 0; i < lanesOrderedByY.length; i++){
			lanesOrderedByY[i] = getYCoordinateFromALane(process, process.getLaneSet().get(0).getLane().get(i).getId());
		}
		
		Arrays.sort(lanesOrderedByY);
		
		List<String> orderedLanes = getOrderedLanes(process, lanesOrderedByY);
		Set<BPMNShape> orderedShapes = new HashSet<>();
		
		double currentLaneY = 0.0;
		
		for(int i = 0; i < orderedLanes.size(); i++){
			
			BPMNShape laneShape = laneShapeMap.get(orderedLanes.get(i));

			if(i == 0){
				currentLaneY = yDistanceBetweenPools[newProcessList.indexOf(process)] + yDistanceBetweenLanes.get(i);
			}
			
			laneShape.getBounds().setY(currentLaneY);
			currentLaneY += laneShape.getBounds().getHeight();
			orderedShapes.add(laneShape);
		}
		
		System.out.println("################## ORDERED LANES #####################");

		Iterator it = orderedLanes.iterator();

		for (BPMNShape laneShape : orderedShapes) {
			System.out.println("Lane = " + it.next() + " ; YPos = " + laneShape.getBounds().getY() + " ; Height = "
					+ laneShape.getBounds().getHeight());
			newDiagramElement.add(new JAXBElement<BPMNShape>(new QName(namespaceURIDiagram, "BPMNShape"),
					BPMNShape.class, laneShape));
		}
		
	}
	
	/*
	 * Draws the BPMN model lanes
	 */
	private void drawLane(TLane lane, int currentPool, int currentLane) {
		double width = Double.parseDouble(txtFileInformation.get(currentPool).get(0).get(2));
		double height = 0.0;

		for (String laneId : laneMinMaxYMap.keySet()) {
			if (laneId.equals(lane.getId())) {

				height = laneMinMaxYMap.get(laneId)[1] - laneMinMaxYMap.get(laneId)[0];

				// height += extraHeightPoolLane;

				height *= heightPoolGraphScale;
				yDistanceBetweenLanes.add(yDistanceBetweenLanes.get(yDistanceBetweenLanes.size() - 1) + height);

			}
		}

		BPMNShape laneShape = new BPMNShape();
		laneShape.setBpmnElement(new QName(lane.getId()));
		laneShape.setId(bpmnShapeLaneId + bpmnShapeLaneIdCounter);
		bpmnShapeLaneIdCounter++;

		laneShape.setBounds(new Bounds());
		laneShape.setIsHorizontal(true);
		laneShape.getBounds().setX(xBounds + xLaneDistanceFromPoolName);
		laneShape.getBounds().setY(yDistanceBetweenPools[currentPool] + yDistanceBetweenLanes.get(currentLane));

		width *= widthPoolGraphScale;
		width -= xLaneDistanceFromPoolName;
		width += extraWidthPoolLane;

		laneShape.getBounds().setWidth(width);
		laneShape.getBounds().setHeight(height);

		System.out.println("Lane = " + lane.getId() + " ; YPos = " + laneShape.getBounds().getY() + " ; Height = "
				+ laneShape.getBounds().getHeight());

		laneShape.setBPMNLabel(new BPMNLabel());
		laneShape.getBPMNLabel().setBounds(new Bounds());

		laneShapeMap.put(lane.getId(), laneShape);

		newDiagramElement.add(new JAXBElement<BPMNShape>(new QName(namespaceURIDiagram, "BPMNShape"), BPMNShape.class,
				laneShape));
	}
	
	/*
	 * Draws the lanes on the BPMN model then reorders them by the order stated on the dot txt files
	 */
	private void drawLanes() {

		int currentPool = 0;
		int currentLane = 0;

		System.out.println("********************** UNORDERED LANES ****************************");

		for (TProcess process : newProcessList) {
			if (!process.getLaneSet().isEmpty()) {

				for (TLaneSet laneSet : process.getLaneSet()) {

					for (int i = 0; i < laneSet.getLane().size(); i++) {
						TLane lane = laneSet.getLane().get(i);
						drawLane(lane, currentPool, currentLane);
						currentLane++;
					}
				}
				reorderLanes(process);
			}
			currentPool++;
		}	
	}

	/*
	 * Returns a sequence flow id from a known source and target id
	 */
	private String getSequenceFlowId(String sourceId, String targetId) {
		for (TProcess process : newProcessList) {
			for (JAXBElement<? extends TFlowElement> flowElement : process.getFlowElement()) {
				if (flowElement.getValue().getClass().getName().equals("bpmn2.TSequenceFlow")) {
					TSequenceFlow sequenceFlow = (TSequenceFlow) flowElement.getValue();

					TFlowElement source = (TFlowElement) sequenceFlow.getSourceRef();
					TFlowElement target = (TFlowElement) sequenceFlow.getTargetRef();

					if (source.getId().equals(sourceId) && target.getId().equals(targetId)) {
						return sequenceFlow.getId();
					}
				}
			}
		}

		return "";
	}

	/*
	 * Returns a sequence flow from a given id
	 */
	private TSequenceFlow getSequenceFlowFromId(String sequenceFlowId) {
		for (TProcess process : newProcessList) {
			for (int i = 0; i < process.getFlowElement().size(); i++) {
				if (process.getFlowElement().get(i).getValue().getId().equals(sequenceFlowId)) {
					return (TSequenceFlow) process.getFlowElement().get(i).getValue();
				}
			}
		}
		return null;
	}

	/*
	 * Checks if a flow element belongs to a lane
	 */
	private boolean flowElementHasLane(String flowElementId) {

		for (TProcess process : newProcessList) {
			String laneId = LaneUtil.getFlowElementLaneId(process, flowElementId);
			if (laneId != null) {
				return true;
			}
		}

		return false;
	}

	/*
	 * Draws the element shapes on the BPMN model
	 */
	private void drawElementShapes() {

		int currentPool = 0;
		xBounds = 175;

		for (List<List<String>> currentFile : txtFileInformation) {
			for (List<String> currentLine : currentFile) {
				if (currentLine.get(0).equals("node")) {

					BPMNShape shape = new BPMNShape();
					shape.setBpmnElement(new QName(currentLine.get(1)));
					shape.setId(bpmnShapeId + currentLine.get(1));

					double x = Double.parseDouble(currentLine.get(2));
					double y = Double.parseDouble(currentLine.get(3));

					y -= Double.parseDouble(currentLine.get(5)) / 2;

					/*
					 * Translate the shapes of elements that don't belong to a
					 * lane to compensate for the extra height of the pool
					 */
					if (!flowElementHasLane(currentLine.get(1)) && !currentLine.get(1).contains("DataObject")
							&& !currentLine.get(1).contains("DataStore")) {
						y += extraYShapes;
					}

					y *= elementScale;

					double width = Double.parseDouble(currentLine.get(4));
					double height = Double.parseDouble(currentLine.get(5));

					double leftX = x - 0.5 * width;
					leftX *= elementScale;

					shape.setBounds(new Bounds());
					shape.getBounds().setX(leftX + xBounds);
					shape.getBounds().setY(y + yDistanceBetweenPools[currentPool]);

					if (currentLine.get(1).contains("DataStore") || currentLine.get(1).contains("DataObject")) {
						shape.getBounds().setWidth(width * dataScaleWidth);
						shape.getBounds().setHeight(height * dataScaleHeight);
					} else {
						shape.getBounds().setWidth(width * elementScale);
						shape.getBounds().setHeight(height * elementScale);
					}

					shape.setBPMNLabel(new BPMNLabel());
					shape.getBPMNLabel().setBounds(new Bounds());

					elementShapeMap.put(currentLine.get(1), shape);

					newDiagramElement.add(new JAXBElement<BPMNShape>(new QName(namespaceURIDiagram, "BPMNShape"),
							BPMNShape.class, shape));

				} else if (currentLine.get(0).equals("edge")) {

					String sequenceFlowId = getSequenceFlowId(currentLine.get(1), currentLine.get(2));

					if (currentLine.get(1).contains("DataObject") || currentLine.get(2).contains("DataObject")
							|| currentLine.get(1).contains("DataStore") || currentLine.get(2).contains("DataStore")) {

						dataObjectExist = true;

					} else {
						TSequenceFlow sequenceFlow = getSequenceFlowFromId(sequenceFlowId);
						TFlowElement source = (TFlowElement) sequenceFlow.getSourceRef();
						TFlowElement target = (TFlowElement) sequenceFlow.getTargetRef();
						createEdge(source.getId(), target.getId(), sequenceFlowId);
					}

				}
			}
			currentPool++;
		}
	}

	/*
	 * Returns the corresponding coordinate value of an element shape plus or minus its width so that edges only touch the exterior part of the shape
	 */
	private double getValueFromCoordinateFromElementShapeMap(String coordinate, String node) {

		for (Map.Entry<String, BPMNShape> entry : elementShapeMap.entrySet()) {
			if (entry.getKey().equals(node)) {

				if (coordinate.equals("x")) {
					return entry.getValue().getBounds().getX() + entry.getValue().getBounds().getWidth();
				} else if (coordinate.equals("xx")) {
					return entry.getValue().getBounds().getX() - entry.getValue().getBounds().getWidth();
				} else if (coordinate.equals("y")) {
					return entry.getValue().getBounds().getY();
				}

			}
		}

		return -1;
	}

	/*
	 * Draws an edge for a given source, target and edge id
	 */
	private void createEdge(String sourceId, String targetId, String edgeId) {
		double sourceX = getValueFromCoordinateFromElementShapeMap("x", sourceId);
		double sourceY = getValueFromCoordinateFromElementShapeMap("y", sourceId);
		double targetX = getValueFromCoordinateFromElementShapeMap("xx", targetId);
		double targetY = getValueFromCoordinateFromElementShapeMap("y", targetId);

		BPMNEdge edge = new BPMNEdge();
		edge.setSourceElement(new QName(bpmnShapeId + sourceId));
		edge.setTargetElement(new QName(bpmnShapeId + targetId));
		edge.setBpmnElement(new QName(edgeId));
		edge.setId(bpmnEdgeId + edgeId);

		Point p1 = new Point();
		Point p2 = new Point();

		p1.setX(sourceX);
		p1.setY(sourceY + centerSequenceFlows);
		p2.setX(targetX);
		p2.setY(targetY + centerSequenceFlows);

		edge.getWaypoint().add(p1);
		edge.getWaypoint().add(p2);

		edge.setBPMNLabel(new BPMNLabel());
		edge.getBPMNLabel().setBounds(new Bounds());

		newDiagramElement.add(new JAXBElement<BPMNEdge>(new QName(namespaceURIDiagram, "BPMNEdge"), BPMNEdge.class,
				edge));
	}

	private void drawMessageFlows() {
		for (TMessageFlow mf : collaboration.getMessageFlow()) {
			createEdge(mf.getSourceRef().getLocalPart(), mf.getTargetRef().getLocalPart(), mf.getId());
		}
	}

	private void drawDataAssociations() {
		if (dataObjectExist) {

			for (int a = 0; a < associationPairList.size(); a++) {
				TAssociation association = associationPairList.get(a).getAssociation();
				createEdge(association.getSourceRef().getLocalPart(), association.getTargetRef().getLocalPart(),
						association.getId());
			}

			for (TProcess process : newProcessList) {
				for (int i = 0; i < process.getFlowElement().size(); i++) {
					TFlowElement flowElement = process.getFlowElement().get(i).getValue();

					if (FlowElementUtil.isDataTask(flowElement)) {
						TActivity activity = (TActivity) flowElement;

						if (!activity.getDataOutputAssociation().isEmpty()) {
							for (int d = 0; d < activity.getDataOutputAssociation().size(); d++) {
								if (activity.getDataOutputAssociation().get(d).getTargetRef().getClass().getName()
										.equals("bpmn2.TDataObject")) {
									TDataObject dataObject = (TDataObject) activity.getDataOutputAssociation().get(d)
											.getTargetRef();
									createEdge(activity.getId(), dataObject.getId(), activity
											.getDataOutputAssociation().get(d).getId());
								} else if (activity.getDataOutputAssociation().get(d).getTargetRef().getClass()
										.getName().equals("bpmn2.TDataStoreReference")) {
									TDataStoreReference dataStoreReference = (TDataStoreReference) activity
											.getDataOutputAssociation().get(d).getTargetRef();
									createEdge(activity.getId(), dataStoreReference.getId(), activity
											.getDataOutputAssociation().get(d).getId());
								} else if (activity.getDataOutputAssociation().get(d).getSourceRef().getClass()
										.getName().equals("java.util.ArrayList")) {

									List<JAXBElement<Object>> jaxbElementList = (ArrayList<JAXBElement<Object>>) activity
											.getDataOutputAssociation().get(d).getSourceRef();

									for (int j = 0; j < jaxbElementList.size(); j++) {

										TDataObject dataObject = (TDataObject) jaxbElementList.get(j).getValue();

										createEdge(activity.getId(), dataObject.getId(), activity
												.getDataOutputAssociation().get(d).getId());
									}

								} else if (activity.getDataOutputAssociation().get(d).getTargetRef().getClass()
										.getName().equals("java.util.ArrayList")) {

									List<JAXBElement<Object>> jaxbElementList = (ArrayList<JAXBElement<Object>>) activity
											.getDataOutputAssociation().get(d).getTargetRef();

									for (int j = 0; j < jaxbElementList.size(); j++) {

										TDataObject dataObject = (TDataObject) jaxbElementList.get(j).getValue();

										createEdge(activity.getId(), dataObject.getId(), activity
												.getDataOutputAssociation().get(d).getId());
									}
								}

							}

						}

						if (!activity.getDataInputAssociation().isEmpty()) {
							for (int d = 0; d < activity.getDataInputAssociation().size(); d++) {
								if (activity.getDataInputAssociation().get(d).getTargetRef().getClass().getName()
										.equals("bpmn2.TDataObject")) {
									TDataObject dataObject = (TDataObject) activity.getDataInputAssociation().get(d)
											.getSourceRef().get(0).getValue();
									createEdge(dataObject.getId(), activity.getId(), activity.getDataInputAssociation()
											.get(d).getId());
								} else if (activity.getDataInputAssociation().get(d).getTargetRef().getClass()
										.getName().equals("bpmn2.TDataStoreReference")) {
									TDataStoreReference dataStoreReference = (TDataStoreReference) activity
											.getDataInputAssociation().get(d).getSourceRef().get(0).getValue();
									createEdge(dataStoreReference.getId(), activity.getId(), activity
											.getDataInputAssociation().get(d).getId());
								} else if (activity.getDataInputAssociation().get(d).getSourceRef().getClass()
										.getName().equals("java.util.ArrayList")) {

									List<JAXBElement<Object>> jaxbElementList = (ArrayList<JAXBElement<Object>>) activity
											.getDataInputAssociation().get(d).getSourceRef();

									for (int j = 0; j < jaxbElementList.size(); j++) {
										if (jaxbElementList.get(j).getValue().getClass().getName()
												.equals("bpmn2.TDataObject")) {
											TDataObject dataObject = (TDataObject) jaxbElementList.get(j).getValue();
											createEdge(activity.getId(), dataObject.getId(), activity
													.getDataInputAssociation().get(d).getId());
										} else if (jaxbElementList.get(j).getValue().getClass().getName()
												.equals("bpmn2.TDataStore")) {
											TDataStore dataStore = (TDataStore) jaxbElementList.get(j).getValue();
											createEdge(activity.getId(), dataStore.getId(), activity
													.getDataInputAssociation().get(d).getId());
										}
									}

								} else if (activity.getDataInputAssociation().get(d).getTargetRef().getClass()
										.getName().equals("java.util.ArrayList")) {

									List<JAXBElement<Object>> jaxbElementList = (ArrayList<JAXBElement<Object>>) activity
											.getDataInputAssociation().get(d).getTargetRef();

									for (int j = 0; j < jaxbElementList.size(); j++) {
										if (jaxbElementList.get(j).getValue().getClass().getName()
												.equals("bpmn2.TDataObject")) {
											TDataObject dataObject = (TDataObject) jaxbElementList.get(j).getValue();
											createEdge(activity.getId(), dataObject.getId(), activity
													.getDataInputAssociation().get(d).getId());
										} else if (jaxbElementList.get(j).getValue().getClass().getName()
												.equals("bpmn2.TDataStore")) {
											TDataStore dataStore = (TDataStore) jaxbElementList.get(j).getValue();
											createEdge(activity.getId(), dataStore.getId(), activity
													.getDataInputAssociation().get(d).getId());
										}
									}
								}
							}
						}
					}

				}
			}
		}
	}

	private void getSelectedFileName() {

		JFileChooser fileChooser = new JFileChooser();
		File file;

		file = fileChooser.getSelectedFile();

		System.out.println("Selected file is: " + file.getAbsolutePath() + "/" + file.getName());

	}

	protected List<JAXBElement<? extends DiagramElement>> draw() {
		initiateDrawProcess();
		drawPools();

		if (processListHasLanes) {
			drawLanes();
		}
		drawElementShapes();
		drawMessageFlows();

		if (dataObjectExist) {
			drawDataAssociations();
		}

		// getSelectedFileName();

		return newDiagramElement;
	}

}
