import requests

# script for testing

def req_loc(action, body):
    resp = requests.post("http://localhost:8080/mail2", json={
        "action": action,
        "body": body
    })
    if resp.status_code == 200:
        return resp.json()
    raise ValueError(resp.text)

def req_rem(action, body):
    resp = requests.post("http://localhost:8081/mail2", json={
        "action": action,
        "body": body
    })
    if resp.status_code == 200:
        return resp.json()
    raise ValueError(resp.text)

def auth_loc():
    return req_loc("auth", {
        "address": "http://alice@localhost:8080/mail2",
        "password": ""
    })

def auth_rem():
    return req_rem("auth", {
        "address": "http://bob@localhost:8081/mail2",
        "password": ""
    })

def send_to_loc(token):
    return req_loc("send", {
        "token": token,
        "from": "http://alice@localhost:8080/mail2",
        "to": ["http://bob@localhost:8080/mail2"],
        "data": "alice to bob"
    })

def send_to_rem(token):
    return req_loc("send", {
        "token": token,
        "from": "http://alice@localhost:8080/mail2",
        "to": ["http://bob@localhost:8081/mail2"],
        "data": "alice to remote bob"
    })

def receive_loc(token):
    return req_loc("receive", {
        "token": token,
    })

def receive_rem(token):
    return req_rem("receive", {
        "token": token,
    })

if __name__ == "__main__":
    resp_loc = auth_loc()
    resp_rem = auth_rem()
    if resp_loc["status"] == "ok" and resp_rem["status"] == "ok":
        token_loc = resp_loc["token"]
        token_rem = resp_rem["token"]
        # print(send_to_loc(token_loc))
        # print(send_to_rem(token_loc))
        # print(receive_loc(token_loc))
        print(receive_rem(token_rem))
    else:
        print(resp_loc["message"], resp_rem["message"])

