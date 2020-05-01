import numpy as np
import sys
import pandas as pd
import pickle
import requests
def main():
    start_lat,start_lng,end_lat,end_lng = map(str,sys.argv[1:])
    URL = 'https://maps.googleapis.com/maps/api/directions/json?origin='+start_lat+','+start_lng+'&destination='+end_lat+','+end_lng+'&alternatives=true&key=<ENTER YOUR API KEY>'
    # sending get request and saving the response as response object
    r = requests.get(url = URL)

    # extracting data in json format 
    data = r.json()
    waypoints = []
    crime_score_arr = []
    loaded_model = pickle.load(open('finalized_model(200).sav', 'rb'))
    df = pd.read_csv('clustered_data.csv')
    for route in data['routes']:
        tempwaypoints = []
        crime_score = 0
        for step in route['legs'][0]['steps']:
            start_lat = step['start_location']['lat']
            start_lng = step['start_location']['lng']
            end_lat = step['end_location']['lat']
            end_lng = step['end_location']['lng']
            pred_start = loaded_model.predict([[float(start_lat),float(start_lng)]])
            pred_end = loaded_model.predict([[float(end_lat),float(end_lng)]])
            crime_score += float(df['Factor'][pred_start])/1000 + float(df['Factor'][pred_end])/1000
            tempwaypoints.append([start_lat,start_lng])
            tempwaypoints.append([end_lat,end_lng])
        waypoints.append(tempwaypoints)
        crime_score_arr.append(crime_score)
    try:
        print(waypoints[crime_score_arr.index(min(crime_score_arr))])
    except ValueError:
        print("error occurred")
if __name__ ==  "__main__":
    main()