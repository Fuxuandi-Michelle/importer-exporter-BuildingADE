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

public enum DBSequencerEnum {
	ADDRESS_ID_SEQ,
	APPEARANCE_ID_SEQ,
	CITYOBJECT_ID_SEQ,
	SURFACE_GEOMETRY_ID_SEQ,
	IMPLICIT_GEOMETRY_ID_SEQ,
	
	//MultiGeometry
	POINT_LINE_GEOMETRY_ID_SEQ,
	SURFACE_OF_MULTI_GEOMETRY_ID_SEQ,
	MULTI_GEOMETRY_ID_SEQ,
	
	SURFACE_DATA_ID_SEQ,
	TEX_IMAGE_ID_SEQ,
	CITYOBJECT_GENERICATTRIB_ID_SEQ,
	EXTERNAL_REFERENCE_ID_SEQ,
	RASTER_REL_GEORASTER_ID_SEQ
}
