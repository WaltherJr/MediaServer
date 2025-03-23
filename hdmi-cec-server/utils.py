from flask import jsonify, request, make_response
from subprocess import PIPE, run
import re
import time

FIRST_HDMI_CHANNEL = 1
LAST_HDMI_CHANNEL = 3
INVALID_REQUEST_PARAMETER_VALUE = "Invalid value \"{parameterValue}\" for request parameter \"{parameterName}\""
MANDATORY_REQUEST_PARAMETER_IS_MISSING = "Mandatory request parameter \"{parameterName}\" is missing"
ERROR_ADAPTER_NOT_CONFIGURED = "Adapter is unconfigured, please configure it first."

def validate_to_channel_parameter(request):
    return utils.validate_parameter(request.args.get("to"), "to", "^[0-9]$", "--to {parameter}", False)

def validate_parameter(parameterValue, parameterName, validParameterRegex, returnValueStringTemplate, mandatory = False):
    if (parameterValue):
        parameterValueStr = str(parameterValue)
        if (not re.match(validParameterRegex, parameterValueStr)):
            raise ValidationError(INVALID_REQUEST_PARAMETER_VALUE.format(parameterValue = parameterValueStr, parameterName = parameterName))
        else:
            return returnValueStringTemplate.format(parameter = parameterValueStr)
    elif (mandatory):
        raise ValidationError(MANDATORY_REQUEST_PARAMETER_IS_MISSING.format(parameterName = parameterName))

def issue_hdmi_cec_command(command):
    result1 = run(command, stdout = PIPE, stderr = PIPE, universal_newlines = True, shell=True)
    result1_stdout = result1.stdout.strip()
    result1_stderr = result1.stderr.strip()
    not_configured_adapter_message = re.search(ERROR_ADAPTER_NOT_CONFIGURED, result1_stderr)
    not_visible_adapter_message = re.search("Sequence: .+ Timestamp: .+$", result1_stdout)
    adapter_error_message = None
    if not_configured_adapter_message:
        adapter_error_message = "TV adapter is unconfigured, configuring..."
    elif not not_visible_adapter_message:
        adapter_error_message = "TV adapter is not visible, fixing..."
    if not_configured_adapter_message or not not_visible_adapter_message:
        configure_tv_result = configure_hdmi_cec()
        print("TV adapter is now configured/visible, running command one more time")
        time.sleep(1)
        result2 = run(command, stdout = PIPE, stderr = PIPE, universal_newlines = True, shell=True)
        return [{"command": command, "stdout": result1.stdout, "stderr": result1.stderr}, configure_tv_result, {"command": command, "stdout": result2.stdout, "stderr": result2.stderr}]
    return {"command": command, "stdout": result1.stdout, "stderr": result1.stderr}

def configure_hdmi_cec():
    command = "cec-ctl -d/dev/cec0 --tv -S"
    result = run(command, stdout = PIPE, stderr = PIPE, universal_newlines = True, shell=True)
    return {"command": command, "stdout": result.stdout, "stderr": result.stderr}

def create_response(statusCode, responseJSON, responseHeaders = None):
    response = make_response(jsonify(responseJSON))
    response.status_code = statusCode
    response.mimetype = "application/json"
    if (responseHeaders):
        for key, value in responseHeaders.items():
            response.headers[key] = value
    return response

def create_error_json_response(statusCode, response):
    return create_response(statusCode, {"responseType": "ERROR", "response": response})

def create_success_json_response(statusCode, response, responseHeaders = {"Access-Control-Allow-Origin": "*"}):
    return create_response(statusCode, {"responseType": "SUCCESS","response": response}, responseHeaders)

def validate_to_channel_parameter(request):
    return validate_parameter(request.args.get("to"), "to", "^[0-9]$", "--to {parameter}", False)

def validate_parameter(parameterValue, parameterName, validParameterRegex, returnValueStringTemplate, mandatory = False):
    if (parameterValue):
        parameterValueStr = str(parameterValue)
        if (not re.match(validParameterRegex, parameterValueStr)):
            raise ValidationError(INVALID_REQUEST_PARAMETER_VALUE.format(parameterValue = parameterValueStr, parameterName = parameterName))
        else:
            return returnValueStringTemplate.format(parameter = parameterValueStr)
    elif (mandatory):
        raise ValidationError(MANDATORY_REQUEST_PARAMETER_IS_MISSING.format(parameterName = parameterName))
