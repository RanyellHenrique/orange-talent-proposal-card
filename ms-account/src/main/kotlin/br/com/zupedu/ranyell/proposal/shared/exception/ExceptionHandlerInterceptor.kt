package br.com.zupedu.ranyell.proposal.shared.exception

import com.google.rpc.BadRequest
import com.google.rpc.Code
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.StatusProto
import io.grpc.stub.StreamObserver
import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import javax.validation.ConstraintViolationException

@Singleton
@InterceptorBean(HandlerError::class)
class ExceptionHandlerInterceptor : MethodInterceptor<Any, Any> {

    private val LOGGER = LoggerFactory.getLogger(this.javaClass)


    override fun intercept(context: MethodInvocationContext<Any, Any>): Any? {
        return try {
            context.proceed()
        } catch (e: Exception) {
            val status = when (e) {
                is ConstraintViolationException -> handleConstraintViolationException(e)
                is ResourceNotFoundException -> Status.NOT_FOUND.withDescription(e.message).asRuntimeException()
                is ResourceAlreadyExistingException -> Status.ALREADY_EXISTS.withDescription(e.message).asRuntimeException()
                else -> Status.UNKNOWN.withDescription("An unexpected error happened").asRuntimeException()
            }
            val responseObserver = context.parameterValues[1] as StreamObserver<*>
            responseObserver.onError(status)
        }
    }

    private fun handleConstraintViolationException(e: ConstraintViolationException): StatusRuntimeException {
        val details = BadRequest.newBuilder()
            .addAllFieldViolations(e.constraintViolations.map {
                BadRequest.FieldViolation.newBuilder()
                    .setField(it.propertyPath.last().name)
                    .setDescription(it.message)
                    .build()
            })
            .build()

        val statusProto = com.google.rpc.Status.newBuilder()
            .setCode(Code.INVALID_ARGUMENT_VALUE)
            .setMessage("invalid parameters")
            .addDetails(com.google.protobuf.Any.pack(details))
            .build()

        LOGGER.info("statusProto: $statusProto")
        val error = StatusProto.toStatusRuntimeException(statusProto)
        return error
    }
}