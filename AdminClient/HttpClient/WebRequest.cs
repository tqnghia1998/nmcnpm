using Newtonsoft.Json;
using System;
using System.IO;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using DbModel;

namespace AdminClient
{
    /// <summary>
    /// Http client để gửi request và nhận respone từ server
    /// </summary>
    public static class WebRequest<T>
    {
        #region Methods

        /// <summary>
        /// Lệnh post
        /// </summary>
        /// <param name="url"></param>
        /// <param name="data"></param>
        /// <returns></returns>
       public static async Task<HttpResult>  PostAsync(string url, T data)
        {
            var DATA = JsonConvert.SerializeObject(data);
            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
            request.Method = "POST";
            request.ContentType = "application/json";
            request.ContentLength = DATA.Length;

            using (Stream webStream = request.GetRequestStream())
            {
                using (StreamWriter requestWriter = new StreamWriter(webStream, Encoding.ASCII))
                {
                    requestWriter.Write(DATA);
                }
            }

            HttpResult result = new HttpResult();

            try
            {
                var temp = await request.GetResponseAsync();
                HttpWebResponse webResponse = (HttpWebResponse)temp;
                result.StatusCode = (int)webResponse.StatusCode;
                using (Stream webStream = webResponse.GetResponseStream())
                {
                    if (null != webStream)
                    {
                        using (StreamReader responseReader = new StreamReader(webStream))
                        {
                            result.MessageResponse = responseReader.ReadToEnd();
                        }
                    }
                }
            }
            catch (WebException ex)
            {
                var response = (HttpWebResponse)ex.Response;
                result.StatusCode = (int)response.StatusCode;

                using(Stream webStream = response.GetResponseStream())
                {
                    if (null != webStream)
                    {
                        using (StreamReader responseReader = new StreamReader(webStream))
                        {
                            result.MessageResponse = responseReader.ReadToEnd();
                        }
                    }
                }
            }

            return result;
        }

        #endregion
    }
}
