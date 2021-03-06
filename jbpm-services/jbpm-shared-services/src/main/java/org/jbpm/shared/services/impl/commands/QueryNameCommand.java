package org.jbpm.shared.services.impl.commands;

import java.util.Map;

import org.drools.core.command.impl.GenericCommand;
import org.jbpm.shared.services.impl.JpaPersistenceContext;
import org.kie.internal.command.Context;

public class QueryNameCommand<T> implements GenericCommand<T> {

	private static final long serialVersionUID = -4014807273522465028L;

	private Class<T> resultType;
	private String queryName;
	private Map<String, Object> params;
	
	public QueryNameCommand(String queryName) {
		this.queryName = queryName;
		this.resultType = (Class<T>) Object.class.getClass();
	}
	
	public QueryNameCommand(String queryName, Map<String, Object> params) {
		this.queryName = queryName;
		this.params = params;
		this.resultType = (Class<T>) Object.class.getClass();
	}
	
	@Override
	public T execute(Context context) {
		JpaPersistenceContext ctx = (JpaPersistenceContext) context;
		if (params == null) {
			return ctx.queryInTransaction(queryName, resultType);
		} else {
			return ctx.queryWithParametersInTransaction(queryName, params, resultType);
		}
	}


}
