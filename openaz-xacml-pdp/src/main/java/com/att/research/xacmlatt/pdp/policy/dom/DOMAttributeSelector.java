/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */

/*
 *                        AT&T - PROPRIETARY
 *          THIS FILE CONTAINS PROPRIETARY INFORMATION OF
 *        AT&T AND IS NOT TO BE DISCLOSED OR USED EXCEPT IN
 *             ACCORDANCE WITH APPLICABLE AGREEMENTS.
 *
 *          Copyright (c) 2013 AT&T Knowledge Ventures
 *              Unpublished and Not for Publication
 *                     All Rights Reserved
 */
package com.att.research.xacmlatt.pdp.policy.dom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.att.research.xacml.api.Identifier;
import com.att.research.xacml.api.XACML3;
import com.att.research.xacml.std.StdStatusCode;
import com.att.research.xacml.std.dom.DOMProperties;
import com.att.research.xacml.std.dom.DOMStructureException;
import com.att.research.xacml.std.dom.DOMUtil;
import com.att.research.xacmlatt.pdp.policy.expressions.AttributeSelector;

/**
 * DOMAttributeSelector extends {@link com.att.research.xacmlatt.pdp.policy.expressions.AttributeSelector} with methods
 * for creation from DOM {@link org.w3c.dom.Node}s.
 * 
 * @author car
 * @version $Revision: 1.3 $
 */
public class DOMAttributeSelector extends AttributeSelector {
        private static Log logger	= LogFactory.getLog(DOMAttributeSelector.class);
        
        protected DOMAttributeSelector() {
        }

        /**
         * Creates a new <code>DOMAttributeSelector</code> by parsing the given <code>Node</code> representing a XACML AttributeSelector element.
         * 
         * @param nodeAttributeSelector the <code>Node</code> representing the XACML AttributeSelector element
         * @return a new <code>DOMAttributeSelector</code> parsed from the given <code>Node</code>.
         * @throws DOMStructureException if there is an error parsing the <code>Node</code>
         */
        public static AttributeSelector newInstance(Node nodeAttributeSelector) throws DOMStructureException {
                Element elementAttributeSelector			= DOMUtil.getElement(nodeAttributeSelector);
                boolean bLenient							= DOMProperties.isLenient();
                
                DOMAttributeSelector domAttributeSelector	= new DOMAttributeSelector();
                
                try {
                        domAttributeSelector.setCategory(DOMUtil.getIdentifierAttribute(elementAttributeSelector, XACML3.ATTRIBUTE_CATEGORY, !bLenient));
                        
                        Identifier identifier;			
                        if ((identifier = DOMUtil.getIdentifierAttribute(elementAttributeSelector, XACML3.ATTRIBUTE_CONTEXTSELECTORID)) != null) {
                                domAttributeSelector.setContextSelectorId(identifier);
                        }
                        
                        domAttributeSelector.setPath(DOMUtil.getStringAttribute(elementAttributeSelector, XACML3.ATTRIBUTE_PATH, !bLenient));
                        domAttributeSelector.setDataTypeId(DOMUtil.getIdentifierAttribute(elementAttributeSelector, XACML3.ATTRIBUTE_DATATYPE, !bLenient));
                        Boolean mustBePresent	= DOMUtil.getBooleanAttribute(elementAttributeSelector, XACML3.ATTRIBUTE_MUSTBEPRESENT, !bLenient);
                        if (mustBePresent != null) {
                                domAttributeSelector.setMustBePresent(mustBePresent);
                        }
                } catch (DOMStructureException ex) {
                        domAttributeSelector.setStatus(StdStatusCode.STATUS_CODE_SYNTAX_ERROR, ex.getMessage());
                        if (DOMProperties.throwsExceptions()) {
                                throw ex;
                        }
                }
                
                return domAttributeSelector;
        }
        
        public static boolean repair(Node nodeAttributeSelector) throws DOMStructureException {
                Element elementAttributeSelector	= DOMUtil.getElement(nodeAttributeSelector);
                boolean result						= false;
                
                result								= DOMUtil.repairIdentifierAttribute(elementAttributeSelector, XACML3.ATTRIBUTE_CATEGORY, logger) || result;
                result								= DOMUtil.repairStringAttribute(elementAttributeSelector, XACML3.ATTRIBUTE_PATH, "/", logger) || result;
                result								= DOMUtil.repairIdentifierAttribute(elementAttributeSelector, XACML3.ATTRIBUTE_DATATYPE, logger) || result;
                result								= DOMUtil.repairBooleanAttribute(elementAttributeSelector, XACML3.ATTRIBUTE_MUSTBEPRESENT, false, logger) || result;
                
                return result;
        }
}
