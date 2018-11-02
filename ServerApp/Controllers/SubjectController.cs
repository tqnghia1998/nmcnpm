using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using DbModel;
using Microsoft.AspNetCore.Mvc;

namespace ServerApp
{
    
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

        [Route("api/subject")]
        [HttpPost]
        public IActionResult PostSubject([FromBody] CreateSubjectCredentialsDataModel data)
        {
            try
            {
                mContext.Subject.Add(data.Subject);
                mContext.SaveChanges();
            }
            
            catch(Exception ex)
            {
                return BadRequest("Tạo môn học thất bại. \nCó thể mã học phần bị trùng hoặc chưa điền đầy đủ các thông tin cần thiết.");
            }

            mContext.Schedules.AddRange(data.Schedule);
            mContext.SaveChanges();
            

            return Ok();
        }

        // Get subjects
        [Route("api/subject")]
        [HttpGet]
        public ActionResult<List<SubjectDataModel>> GetSubjects()
        {
            return mContext.Subject.ToList();
        }

        [HttpGet("api/subject/{id}")]
        public ActionResult<List<SubjectDataModel>> GetSubjectById(String id, [FromQuery] String field)
        {
            List<SubjectDataModel> list = new List<SubjectDataModel>();
            SubjectDataModel item = mContext.Subject.Find(id);
            if (item == null)
            {
                IQueryable<SubjectDataModel> _list = mContext.Subject;
                if (field == "major") _list = _list.Where(x => x.Major == id);
                if (field == "subject") _list = _list.Where(x => x.Subject == id);
                if (field == "credit") _list = _list.Where(x => x.Credit == int.Parse(id));
                if (field == "teacher") _list = _list.Where(x => x.Teacher == id);
                if (field == "timestart") _list = _list.Where(x => x.TimeStart == id);
                if (field == "timefinish") _list = _list.Where(x => x.TimeFinish == id);
                if (field == "status") _list = _list.Where(x => x.Status == int.Parse(id));
                if (_list != mContext.Subject) return _list.ToList();
            }
            list.Add(item);
            return list;
        }

        // Get schedules
        [Route("api/subject/schedule")]
        [HttpGet]
        public ActionResult<List<ScheduleDataModel>> GetSchedules()
        {
            return mContext.Schedules.ToList();
        }

        [HttpGet("api/subject/schedule/{id}/{day}")]
        public ActionResult<List<ScheduleDataModel>> GetScheduleById(String id, String day, [FromQuery] String field)
        {
            List<ScheduleDataModel> list = new List<ScheduleDataModel>();
            ScheduleDataModel item = mContext.Schedules.Find(id, day);
            list.Add(item);
            return list;
        }
    }
}