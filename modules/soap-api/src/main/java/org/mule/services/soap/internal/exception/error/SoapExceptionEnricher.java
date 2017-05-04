/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.services.soap.internal.exception.error;

import static org.mule.services.soap.api.exception.error.SoapErrors.BAD_REQUEST;
import static org.mule.services.soap.api.exception.error.SoapErrors.BAD_RESPONSE;
import static org.mule.services.soap.api.exception.error.SoapErrors.CANNOT_DISPATCH;
import static org.mule.services.soap.api.exception.error.SoapErrors.ENCODING;
import static org.mule.services.soap.api.exception.error.SoapErrors.INVALID_WSDL;
import static org.mule.services.soap.api.exception.error.SoapErrors.SOAP_FAULT;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.services.soap.api.exception.BadRequestException;
import org.mule.services.soap.api.exception.BadResponseException;
import org.mule.services.soap.api.exception.DispatchingException;
import org.mule.services.soap.api.exception.EncodingException;
import org.mule.services.soap.api.exception.InvalidWsdlException;
import org.mule.services.soap.api.exception.SoapFaultException;
import org.mule.services.soap.api.exception.SoapServiceException;

/**
 * A util class that knows how to wrap an exception throwed by the Soap Service into a {@link ModuleException}.
 * <p>
 * This class is internal, and it's not supposed to be used by others.
 *
 * @since 4.0
 */
public class SoapExceptionEnricher {

  /**
   * Wraps an exception in a {@link ModuleException} specifying an error type.
   *
   * @param e the exception to be wrapped in a {@link ModuleException}
   * @return a new {@link ModuleException} with the corresponding error type.
   */
  public Exception enrich(Exception e) {
    if (e instanceof SoapFaultException) {
      return new ModuleException(e, SOAP_FAULT);
    }
    if (e instanceof InvalidWsdlException) {
      return new ModuleException(e, INVALID_WSDL);
    }
    if (e instanceof BadResponseException) {
      return new ModuleException(e, BAD_RESPONSE);
    }
    if (e instanceof BadRequestException) {
      return new ModuleException(e, BAD_REQUEST);
    }
    if (e instanceof DispatchingException) {
      return new ModuleException(e, CANNOT_DISPATCH);
    }
    if (e instanceof EncodingException) {
      return new ModuleException(e, ENCODING);
    }
    return new Exception("Unexpected error while consuming web service: " + e.getMessage(), e);
  }
}