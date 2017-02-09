/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.routing;

import org.mule.runtime.api.meta.AbstractAnnotatedObject;
import org.mule.runtime.core.api.MuleContext;
import org.mule.runtime.core.api.Event;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.core.api.context.MuleContextAware;
import org.mule.runtime.api.lifecycle.Initialisable;
import org.mule.runtime.api.lifecycle.InitialisationException;
import org.mule.runtime.core.api.processor.Processor;


/**
 *
 * Routes a message through a set of routes that will be obtained dynamically (per message) using a {@link DynamicRouteResolver}.
 *
 * The message will be route to the first route, if the route execution is successful then execution ends, if not the message will
 * be route to the next route. This continues until a successful route is found.
 *
 */
public class DynamicFirstSuccessful extends AbstractAnnotatedObject implements Processor, Initialisable, MuleContextAware {

  private FirstSuccessfulRoutingStrategy routingStrategy;
  private MuleContext muleContext;
  private String failureExpression;
  private DynamicRouteResolver dynamicRouteResolver;


  @Override
  public Event process(Event event) throws MuleException {
    return routingStrategy.route(event, dynamicRouteResolver.resolveRoutes(event));
  }

  @Override
  public void initialise() throws InitialisationException {
    routingStrategy = new FirstSuccessfulRoutingStrategy(muleContext, failureExpression, (route, event) -> route.process(event));
  }

  @Override
  public void setMuleContext(MuleContext context) {
    this.muleContext = context;
  }

  /**
   * Specifies an expression that when evaluated as determines if the processing of one a route was a failure or not.
   *
   * @param failureExpression
   * @see org.mule.runtime.core.routing.filters.ExpressionFilter
   */
  public void setFailureExpression(String failureExpression) {
    this.failureExpression = failureExpression;
  }

  /**
   * @param dynamicRouteResolver custom route resolver to use
   */
  public void setDynamicRouteResolver(DynamicRouteResolver dynamicRouteResolver) {
    this.dynamicRouteResolver = dynamicRouteResolver;
  }
}