nothing
=======


####使用说明

MainActivity.java

```

 private void getWechats(){
    try {
      HttpApiV1.getInstance().getWechats(new NothingResponse<WechatPerson>() {
        @Override
        public void onSuccess(List<WechatPerson> response) {
          if(page > 1){
            adapter.getPersons().addAll(response);
          } else {
            adapter.setPersons(response);
          }

          adapter.notifyDataSetChanged();

        }

        @Override
        public void onSuccess(WechatPerson response) {

        }

        @Override
        public void onSuccess(String responseString) {

        }

        @Override
        public void onFailure(int statusCode, Throwable e, JSONObject errorResponse) {

        }
      }, page);
    } catch (Exception e) {
      e.printStackTrace();
    }
```

WechatPerson.java

```
public class WechatPerson  implements Result {

  public String avatar;
  public String name;
  public String wechat;
  public String summary;

  @Override
  public <T extends Result> List<T> transfer(JSONArray jsonArray) {
    List<WechatPerson> items = new ArrayList<WechatPerson>();
    for(int i = 0; i < jsonArray.length(); i++) {
      try {
        WechatPerson item = toBean(jsonArray.getJSONObject(i));
        items.add(item);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return (List<T>) items;
  }

  @Override
  public <T extends Result> T transfer(JSONObject jsonObject) {
    return toBean(jsonObject);
  }

  public static <T extends Result> T toBean(JSONObject jsonObject) {
    WechatPerson obj = new WechatPerson();
    try {
      if (jsonObject.has("avatar")) {
        obj.avatar = jsonObject.getString("avatar");
      }
      if (jsonObject.has("name")) {
        obj.name = jsonObject.getString("name");
      }
      if (jsonObject.has("summary")) {
        obj.summary = jsonObject.getString("summary");
      }
      if (jsonObject.has("wechat")) {
        obj.wechat = jsonObject.getString("wechat");
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return (T) obj;
  }


}
```

HttpApiV1.java

```
public class HttpApiV1 {
  private NothingRestClient mClient;

  private String mApiBaseUrl;

  private static HttpApiV1 instance = null;
  private static String API_WECHATS = "/api/wechats.json";
  public static int MAX_COUNT = 20;


  public static HttpApiV1 getInstance(){
    if(instance == null){
      instance = new HttpApiV1();
    }
    return instance;
  }

  public AsyncHttpClient getAsyncHttpClient() {
    return mClient.getAsyncHttpClient();
  }
  public NothingRestClient getClient(){return mClient;}

  public HttpApiV1() {
    mApiBaseUrl = Settings.BASE_URL;
    mClient = new NothingRestClient(mApiBaseUrl);
  }



  public void getWechats(NothingResponse<WechatPerson> response, int page) throws Exception {
    mClient.get(fullUrl(API_WECHATS), response, new NothingStringParam("page", String.valueOf(page)));
  }



  private String fullUrl(String url) {
    return mApiBaseUrl + url;
  }

}
```
