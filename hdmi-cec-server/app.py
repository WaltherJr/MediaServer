from flask import Flask, jsonify, request, make_response, render_template
from subprocess import PIPE, run
import re
import time
import utils

app = Flask(__name__)

class ValidationError(Exception):
    pass

@app.route("/", methods = ["GET"])
def hello():
    return "Hello from Radxa Rock server!"

@app.route("/hdmi-cec/command-result", methods = ["GET"])
def get_hdmi_cec_command_result():
    try:
        commandString = utils.validate_parameter(request.args.get("command"), "command", "^.+$", "{parameter}", True)
        result = run(commandString, stdout = PIPE, stderr = PIPE, universal_newlines = True, shell=True)
        return utils.create_success_json_response(200, {"command": commandString, "stdout": result.stdout, "stderr": result.stderr})
    except ValidationError as e:
        return utils.create_error_json_response(400, str(e))
    except Exception as e:
        return utils.create_error_json_response(500, str(e))

@app.route("/hdmi-cec/version", methods = ["GET"])
def get_hdmi_cec_version():
    try:
        toChannelString = utils.validate_to_channel_parameter(request)
        command = "cec-ctl" + (f" {toChannelString}" if toChannelString else "") + " -d/dev/cec0 --get-cec-version | awk '/cec-version:/ {print $2}'"
        result = run(command, stdout = PIPE, stderr = PIPE, universal_newlines = True, shell=True)
        return utils.create_success_json_response(200, {"cec-version": result.stdout.strip()})
    except Exception as e:
        return utils.create_error_json_response(500, str(e))

@app.route("/hdmi-cec/configuration", methods = ["GET"])
def get_hdmi_cec_configuration():
    try:
        result = utils.configure_hdmi_cec()
        return utils.create_success_json_response(200, result)
    except Exception as e:
        return utils.create_error_json_response(500, str(e))

@app.route("/hdmi-cec/topology", methods = ["GET"])
def get_hdmi_cec_topology():
    try:
        toChannelString = utils.validate_to_channel_parameter(request)
        command = "cec-ctl" + (f" {toChannelString}" if toChannelString else "") + " -d/dev/cec0 --show-topology"
        result = run(command, stdout = PIPE, stderr = PIPE, universal_newlines = True, shell=True)
        return utils.create_success_json_response(200, {"command": command, "stdout": result.stdout, "stderr": result.stderr})
    except ValidationError as e:
        return utils.create_error_json_response(400, str(e))
    except Exception as e:
        return utils.create_error_json_response(500, str(e))

@app.route("/hdmi/current-channel", methods = ["GET"])
def get_current_hdmi_channel():
    try:
        toChannelString = utils.validate_to_channel_parameter(request)
        command = "cec-ctl" + (f" {toChannelString}" if toChannelString else "") + " --give-physical-addr | awk '/Physical Address[[:space:]]+: ([0-9])/ {print substr($4, 0, 1)}'"
        result = run(command, capture_output=True, text=True, universal_newlines = True, shell=True)
        return utils.create_success_json_response(200, {"channel": int(result.stdout.strip())})
    except ValidationError as e:
        return utils.create_error_json_response(400, str(e))
    except Exception as e:
        return utils.create_error_json_response(500, str(e))

@app.route("/hdmi/current-channel", methods = ["PUT"])
def switch_hdmi_channel():
    try:
        requestBody = request.json
        hdmiChannelString = utils.validate_parameter(requestBody.get("channel"), "channel", "^[{min}-{max}]$".format(min = FIRST_HDMI_CHANNEL, max = LAST_HDMI_CHANNEL), "{parameter}", True)
        toChannelString = utils.validate_to_channel_parameter(request)
        result = utils.issue_hdmi_cec_command("cec-ctl" + (f" {toChannelString}" if toChannelString else "") +  f" --active-source phys-addr={hdmiChannelString}.0.0.0")
        return utils.create_success_json_response(200, result.stdout)

    except ValidationError as e:
        return utils.create_error_json_response(400, str(e))
    except Exception as e:
        return utils.create_error_json_response(500, str(e))


@app.route("/tv/standbystate", methods = ["GET"])
def get_tv_standby_state():
    requestBody = request.json
    standByStateString = utils.validate_parameter(requestBody.get("standbyState"), "standbyState", "^(on|off)$", "{parameter}", True)
    toChannelString = utils.validate_to_channel_parameter(request)
    command = "cec-ctl" + (f" {toChannelString}" if toChannelString else "") + " -d/dev/cec0"
    if standByStateString == "off":
        command += " --standby"
    elif standByStateString == "on":
        command += " --image-view-on"
    result = utils.issue_hdmi_cec_command(command)
    return utils.create_success_json_response(200, result)

'''
@app.route("/tv/standbystate", methods = ["PUT"])
def set_tv_standby_state():
    try:
        toChannelString = validate_to_channel_parameter(request)
        command = "cec-ctl" + (f" {toChannelString}" if toChannelString else "") + " --give-device-power-status | awk '/pwr-state: ([a-z]+) / {print $2}'"
        result = run(command, capture_output=True, text=True, universal_newlines = True, shell=True)
        return create_success_json_response(200, {"state": result.stdout.strip()})
    except ValidationError as e:
        return create_error_json_response(400, str(e))
    except Exception as e:
        return create_error_json_response(500, str(e))
'''

@app.route("/tv/standbystate", methods = ["PUT"])
def set_tv_standby_state():
    try:
        requestBody = request.json
        standByStateString = utils.validate_parameter(requestBody.get("standbyState"), "standbyState", "^(on|off)$", "{parameter}", True)
        toChannelString = utils.validate_to_channel_parameter(request)
        command = "cec-ctl" + (f" {toChannelString}" if toChannelString else "") + " -d/dev/cec0"
        if standByStateString == "off":
            command += " --standby"
        elif standByStateString == "on":
            command += " --image-view-on"

        result = utils.issue_hdmi_cec_command(command)
        return utils.create_success_json_response(200, result)

    except ValidationError as e:
        return utils.create_error_json_response(400, str(e))
    except Exception as e:
        return utils.create_error_json_response(500, str(e))

