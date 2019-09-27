#!/bin/bash


echo "Cleaning elasticsearch...."

curl -XDELETE elasticsearch:9200/demographic
curl -X PUT "elasticsearch:9200/demographic?pretty" -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "properties": {
      "session_id": {"type": "keyword", "index": true},
        "visitor_id": {"type": "keyword", "index": true},
        "time_event": {
            "type": "date",
            "format" : "strict_date_optional_time||epoch_second||epoch_millis"
        },
        "type_event": {"type": "keyword", "index": true},
        "playerkey": {"type": "keyword", "index": true},
        "video_id": {"type": "keyword", "index": true},
        "https": {"type": "boolean"},
        "browser": {"type": "keyword", "index": true},
        "os": {"type": "keyword", "index": true},
        "device": {"type": "keyword", "index": true},
        "host": {"type": "keyword", "index": true},
        "geo": {"type": "geo_point"},
        "country_name": {"type": "keyword", "index": true},
        "city": {"type": "keyword", "index": true},
        "@timestamp": {
            "type": "date",
            "format" : "strict_date_optional_time||epoch_second||epoch_millis"
        }
    }
  }
}
'

curl -XDELETE elasticsearch:9200/behavior
curl -X PUT "elasticsearch:9200/behavior?pretty" -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "properties": {
        "session_id": {"type": "keyword", "index": true},
        "visitor_id": {"type": "keyword", "index": true},
        "time_event": {
            "type": "date",
            "format" : "strict_date_optional_time||epoch_second||epoch_millis"
        },
        "type_event": {"type": "keyword", "index": true},
        "playerkey": {"type": "keyword", "index": true},
        "video_id": {"type": "keyword", "index": true},
        "https": {"type": "boolean"},
        "action": {"type": "keyword", "index": true},
        "content_time": {"type": "float"},
        "content_total_time": {"type": "float"},
        "@timestamp": {
            "type": "date",
            "format" : "strict_date_optional_time||epoch_second||epoch_millis"
        }
    }
  }
}
'

echo "Done Cleaning elasticsearch...."