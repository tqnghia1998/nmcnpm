using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using DbModel;
using Microsoft.AspNetCore.Mvc;

namespace ServerApp
{
    [Route("api/subject")]
    public class SubjectController : Controller
    {
        #region Private Members

        /// <summary>
        /// Database của server
        /// </summary>
        private ApplicationDbContext mContext { set; get; }

        #endregion

        #region Constructor

        /// <summary>
        /// Default Constructor
        /// </summary>
        /// <param name="context"></param>
        public SubjectController(ApplicationDbContext context)
        {
            mContext = context;
            mContext.Database.EnsureCreated();
        }

        #endregion

        [HttpPost]
        public IActionResult PostSubject([FromBody] SubjectDataModel data)
        {
            try
            {
                mContext.Subject.Add(data);
                mContext.SaveChanges();
            }
            
            catch(Exception ex)
            {
                return BadRequest("Tạo môn học thất bại. \nCó thể mã học phần bị trùng hoặc chưa điền đầy đủ các thông tin cần thiết.");
            }

            return Ok();
        }
    }
}