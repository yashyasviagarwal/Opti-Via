import requests
import json
import sys
# get request
def sendPostRequest(reqUrl, apiKey, secretKey, useType, phoneNo, senderId, textMessage):
    req_params = {
    'apikey':apiKey,
    'secret':secretKey,
    'usetype':useType,
    'phone': phoneNo,
    'message':textMessage,
    'senderid':senderId
    }
    return requests.post(reqUrl, req_params)

def main():
    sos_lat,sos_lng = map(str,sys.argv[1:])
    #print(sos_lat,sos_lng)
    URL = 'https://www.sms4india.com/api/v1/sendCampaign'
    # get response
    response = sendPostRequest(URL, '2HP7Q9FGLP3AIFNA5JWR47YLO5HTE75W', '3S6WN4P6ENE5S0S8', 'stage', '8335022832', 'OPTVIA', 'EMERGENCY! OPTIVIA APP USER SAYS: Please Help Me! Here Is My Location: https://www.google.com/maps/search/?api=1&query='+sos_lat+','+sos_lng)
    # print response if you want
    print(response.text)

if __name__ ==  "__main__":
    main()