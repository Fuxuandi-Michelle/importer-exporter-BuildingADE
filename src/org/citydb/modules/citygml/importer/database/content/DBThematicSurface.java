/*
 * 3D City Database - The Open Source CityGML Database
 * http://www.3dcitydb.org/
 * 
 * Copyright 2013 - 2016
 * Chair of Geoinformatics
 * Technical University of Munich, Germany
 * https://www.gis.bgu.tum.de/
 * 
 * The 3D City Database is jointly developed with the following
 * cooperation partners:
 * 
 * virtualcitySYSTEMS GmbH, Berlin <http://www.virtualcitysystems.de/>
 * M.O.S.S. Computer Grafik Systeme GmbH, Taufkirchen <http://www.moss.de/>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.citydb.modules.citygml.importer.database.content;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.citydb.database.TableEnum;
import org.citydb.log.Logger;
import org.citydb.modules.citygml.common.database.xlink.DBXlinkBasic;
import org.citydb.modules.citygml.common.database.xlink.DBXlinkSurfaceGeometry;
import org.citydb.util.Util;
import org.citygml4j.model.citygml.CityGMLClass;
import org.citygml4j.model.citygml.building.AbstractBoundarySurface;
import org.citygml4j.model.citygml.building.AbstractOpening;
import org.citygml4j.model.citygml.building.OpeningProperty;
import org.citygml4j.model.gml.geometry.aggregates.MultiSurfaceProperty;

public class DBThematicSurface implements DBImporter {
	private final Logger LOG = Logger.getInstance();

	private final Connection batchConn;
	private final DBImporterManager dbImporterManager;

	private PreparedStatement psThematicSurface;
	private DBCityObject cityObjectImporter;
	private DBSurfaceGeometry surfaceGeometryImporter;
	private DBOpening openingImporter;

	private int batchCounter;

	public DBThematicSurface(Connection batchConn, DBImporterManager dbImporterManager) throws SQLException {
		this.batchConn = batchConn;
		this.dbImporterManager = dbImporterManager;

		init();
	}

	private void init() throws SQLException {
		StringBuilder stmt = new StringBuilder()
		.append("insert into THEMATIC_SURFACE (ID, OBJECTCLASS_ID, BUILDING_ID, ROOM_ID, BUILDING_INSTALLATION_ID, LOD2_MULTI_SURFACE_ID, LOD3_MULTI_SURFACE_ID, LOD4_MULTI_SURFACE_ID, ")
		.append("STOREY_ID, PODIUM_ID, BEAM_ID,BUILDING_COLUMN_ID, COVERING_ID, FLOW_TERMINAL_ID, RAILING_ID, ")
		.append("RAMP_ID, RAMP_FLIGHT_ID, SLAB_ID, STAIR_ID, STAIR_FLIGHT_ID) values")
		.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		psThematicSurface = batchConn.prepareStatement(stmt.toString());

		surfaceGeometryImporter = (DBSurfaceGeometry)dbImporterManager.getDBImporter(DBImporterEnum.SURFACE_GEOMETRY);
		cityObjectImporter = (DBCityObject)dbImporterManager.getDBImporter(DBImporterEnum.CITYOBJECT);
		openingImporter = (DBOpening)dbImporterManager.getDBImporter(DBImporterEnum.OPENING);
	}

	public long insert(AbstractBoundarySurface boundarySurface, CityGMLClass parent, long parentId) throws SQLException {
		long boundarySurfaceId = dbImporterManager.getDBId(DBSequencerEnum.CITYOBJECT_ID_SEQ);
		if (boundarySurfaceId == 0)
			return 0;

		String origGmlId = boundarySurface.getId();

		// CityObject
		cityObjectImporter.insert(boundarySurface, boundarySurfaceId);

		// BoundarySurface
		// ID
		psThematicSurface.setLong(1, boundarySurfaceId);

		// OBJECTCLASS_ID
		psThematicSurface.setInt(2, Util.cityObject2classId(boundarySurface.getCityGMLClass()));
		
	
		int idIndex = 0;
		// parentId
		switch (parent) {
		case BUILDING:
		case BUILDING_PART:
			psThematicSurface.setLong(3, parentId);
			psThematicSurface.setNull(4, Types.NULL);
			psThematicSurface.setNull(5, Types.NULL);
			break;
		case BUILDING_ROOM:
			psThematicSurface.setNull(3, Types.NULL);
			psThematicSurface.setLong(4, parentId);
			psThematicSurface.setNull(5, Types.NULL);
			break;
		case BUILDING_INSTALLATION:
		case INT_BUILDING_INSTALLATION:
			psThematicSurface.setNull(3, Types.NULL);
			psThematicSurface.setNull(4, Types.NULL);
			psThematicSurface.setLong(5, parentId);
			break;
		case STOREY: idIndex = 9; break;
		case PODIUM: idIndex = 10; break;
		case BEAM: idIndex = 11; break;
		case COLUMN: idIndex = 12; break;
		case COVERING: idIndex = 13; break;
		case FLOW_TERMINAL: idIndex = 14; break;
		case RAILING: idIndex = 15; break;
		case RAMP: idIndex = 16; break;
		case RAMP_FLIGHT: idIndex = 17; break;
		case SLAB: idIndex = 18; break;
		case STAIR: idIndex = 19; break;
		case STAIR_FLIGHT: idIndex = 20; break;
		
		default:
			psThematicSurface.setNull(3, Types.NULL);
			psThematicSurface.setNull(4, Types.NULL);
			psThematicSurface.setNull(5, Types.NULL);
			
			
			
		}
		
		if(idIndex != 0) {
			psThematicSurface.setNull(3, Types.NULL);
			psThematicSurface.setNull(4, Types.NULL);
			psThematicSurface.setNull(5, Types.NULL);
			
			for(int i = 9; i <= 20; i++) {
				if(i == idIndex) 
					psThematicSurface.setLong(idIndex, parentId);
				else 
					psThematicSurface.setNull(i, Types.NULL);
			}
				
		} else {
			
			for(int i = 9; i <= 20; i++) {
				psThematicSurface.setNull(i, Types.NULL);
			}
			
		}
			

		// Geometry
		for (int i = 0; i < 3; i++) {
			MultiSurfaceProperty multiSurfaceProperty = null;
			long multiSurfaceId = 0;

			switch (i) {
			case 0:
				multiSurfaceProperty = boundarySurface.getLod2MultiSurface();
				break;
			case 1:
				multiSurfaceProperty = boundarySurface.getLod3MultiSurface();
				break;
			case 2:
				multiSurfaceProperty = boundarySurface.getLod4MultiSurface();
				break;
			}

			if (multiSurfaceProperty != null) {
				if (multiSurfaceProperty.isSetMultiSurface()) {
					System.out.println("start insert multisurface");
					multiSurfaceId = surfaceGeometryImporter.insert(multiSurfaceProperty.getMultiSurface(), boundarySurfaceId);
					System.out.println("Insert Thematic Surface: lod "+(i+2) + "multisurface, Id: " + multiSurfaceId);
					multiSurfaceProperty.unsetMultiSurface();
				} else {
					// xlink
					String href = multiSurfaceProperty.getHref();

					if (href != null && href.length() != 0) {
						dbImporterManager.propagateXlink(new DBXlinkSurfaceGeometry(
    							href, 
    							boundarySurfaceId, 
    							TableEnum.THEMATIC_SURFACE, 
    							"LOD" + (i + 2) + "_MULTI_SURFACE_ID"));
					}
				}
			}

			if (multiSurfaceId != 0)
				psThematicSurface.setLong(6 + i, multiSurfaceId);
			else
				psThematicSurface.setNull(6 + i, Types.NULL);
		}

		psThematicSurface.addBatch();
		if (++batchCounter == dbImporterManager.getDatabaseAdapter().getMaxBatchSize())
			dbImporterManager.executeBatch(DBImporterEnum.THEMATIC_SURFACE);

		// Openings
		if (boundarySurface.isSetOpening()) {
			for (OpeningProperty openingProperty : boundarySurface.getOpening()) {
				if (openingProperty.isSetOpening()) {
					AbstractOpening opening = openingProperty.getOpening();
					String gmlId = opening.getId();
					long id = openingImporter.insert(opening, boundarySurfaceId);

					if (id == 0) {
						StringBuilder msg = new StringBuilder(Util.getFeatureSignature(
								boundarySurface.getCityGMLClass(), 
								origGmlId));
						msg.append(": Failed to write ");
						msg.append(Util.getFeatureSignature(
								opening.getCityGMLClass(), 
								gmlId));

						LOG.error(msg.toString());
					}

					// free memory of nested feature
					openingProperty.unsetOpening();
				} else {
					// xlink
					String href = openingProperty.getHref();

					if (href != null && href.length() != 0) {
						dbImporterManager.propagateXlink(new DBXlinkBasic(
								boundarySurfaceId,
								TableEnum.THEMATIC_SURFACE,
								href,
								TableEnum.OPENING
								));
					}
				}
			}
		}

		// insert local appearance
		cityObjectImporter.insertAppearance(boundarySurface, boundarySurfaceId);

		return boundarySurfaceId;
	}

	@Override
	public void executeBatch() throws SQLException {
		psThematicSurface.executeBatch();
		batchCounter = 0;
	}

	@Override
	public void close() throws SQLException {
		psThematicSurface.close();
	}

	@Override
	public DBImporterEnum getDBImporterType() {
		return DBImporterEnum.THEMATIC_SURFACE;
	}

}
