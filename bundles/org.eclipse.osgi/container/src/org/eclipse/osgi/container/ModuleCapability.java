/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.osgi.container;

import java.util.*;
import org.eclipse.osgi.container.namespaces.EquinoxNativeEnvironmentNamespace;
import org.osgi.framework.wiring.BundleCapability;

/**
 * An implementation of {@link BundleCapability}.
 * @since 3.10
 */
public final class ModuleCapability implements BundleCapability {
	private final String namespace;
	private final Map<String, String> directives;
	private final Map<String, Object> attributes;
	private final Map<String, Object> transientAttrs;
	private final ModuleRevision revision;

	ModuleCapability(String namespace, Map<String, String> directives, Map<String, Object> attributes, ModuleRevision revision) {
		this.namespace = namespace;
		this.directives = directives;
		this.attributes = attributes;
		this.transientAttrs = EquinoxNativeEnvironmentNamespace.NATIVE_ENVIRONMENT_NAMESPACE.equals(namespace) ? new HashMap<String, Object>(0) : null;
		this.revision = revision;
	}

	@Override
	public ModuleRevision getRevision() {
		return revision;
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	@Override
	public Map<String, String> getDirectives() {
		return directives;
	}

	@Override
	public Map<String, Object> getAttributes() {
		if (transientAttrs == null)
			return attributes;
		Map<String, Object> result = new HashMap<String, Object>(transientAttrs);
		result.putAll(attributes);
		return Collections.unmodifiableMap(result);
	}

	Map<String, Object> getPersistentAttributes() {
		return attributes;
	}

	public void setTransientAttrs(Map<String, ?> transientAttrs) {
		if (transientAttrs == null) {
			throw new UnsupportedOperationException(namespace + ": namespace does not support transient attributes."); //$NON-NLS-1$
		}
		this.transientAttrs.clear();
		this.transientAttrs.putAll(transientAttrs);
	}

	@Override
	public ModuleRevision getResource() {
		return revision;
	}

	@Override
	public String toString() {
		return namespace + ModuleRevision.toString(attributes, false) + ModuleRevision.toString(directives, true);
	}
}