using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using DbModel;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

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

        /// <summary>
        /// Gửi yêu cầu tạo môn học mới, bao gồm cả lịch học
        /// </summary>
        /// <param name="data"></param>
        /// <returns></returns>
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
                return BadRequest("Tạo môn học thất bại." +
                    "\nCó thể mã học phần bị trùng hoặc chưa điền đầy đủ các thông tin cần thiết.");
            }

            mContext.Schedules.AddRange(data.Schedule);
            mContext.SaveChanges();
            
            return Ok();
        }

        /// <summary>
        /// Gửi yêu cầu tạo dữ liệu đăng ký mới
        /// </summary>
        /// <param name="data"></param>
        /// <returns></returns>
        [Route("api/registered")]
        [HttpPost]
        public IActionResult PostRegistered([FromBody] RegisteredDataModel data)
        {

            try
            {
                mContext.Registered.Add(data);
                mContext.SaveChanges();
            }

            catch (Exception ex)
            {
                return BadRequest("Đăng ký môn học thất bại.");
                // return (string)ex.Message;
            }
            return Ok(new { i = 1, x = 2 });
        }

        ///----------------------------------------------------------------------------------------///

        /// <summary>
        /// Gửi yêu cầu lấy thông tin tất cả khoa
        /// </summary>
        /// <returns></returns>
        [Route("api/subject")]
        [HttpGet]
        public List<String> GetSubjects()
        {
            var listMajor = new List<String>();
            // Xét từng môn học trong csdl
            for (int i = 0; i < mContext.Subject.Count(); i++)
            {
                // Nếu khoa của môn học chưa có trong list thì thêm vào
                String curMajor = mContext.Subject.ToList().ElementAtOrDefault(i).Major;
                if (listMajor.Contains(curMajor) == false)
                {
                    listMajor.Add(curMajor);
                }
            }
            return listMajor;
        }

        /// <summary>
        /// Gửi yêu cầu lấy thông tin một môn học
        /// </summary>
        /// <param name="id"></param>
        /// <param name="field"></param>
        /// <returns></returns>
        [HttpGet("api/subject/{subject}")]
        public CreateSubjectCredentialsDataModel GetSubjectById(String subject)
        {
            var model = new CreateSubjectCredentialsDataModel();
            // FirstOrDefault() có nghĩa: Nếu có thì lấy phần tử đầu, không thì null
            model.Subject = mContext.Subject.Where(x => x.Subject == subject).FirstOrDefault();
            // Lấy danh sách lịch học tương ứng
            model.Schedule = mContext.Schedules.Where(x => x.Id == model.Subject.Id).ToList();
            return model;
        }

        /// <summary>
        /// Lấy danh sách môn học theo khoa, năm, học kỳ
        /// </summary>
        /// <param name="course"></param>
        /// <param name="term"></param>
        /// <returns></returns>
        [HttpGet("api/subject/{major}/{course}/{term}")]
        public List<CreateSubjectCredentialsDataModel> GetSubjectByTerm(String major, String course, String term)
        {
            // Tạo danh sách các môn học và lịch học tương ứng (xem class CreateSubjectCredentialsDataModel)
            var listSubject = new List<CreateSubjectCredentialsDataModel>();
            // Tạo danh sách các môn học trước
            List<SubjectDataModel> models = mContext.Subject.Where(x => EF.Functions.Like(x.Major, major)
                                                                     && EF.Functions.Like(x.Course, course)
                                                                     && EF.Functions.Like(x.Term, term))
                                                                     .ToList();
            // Làm tương tự
            for (int i = 0; i < models.Count(); i++)
            {
                var model = new CreateSubjectCredentialsDataModel();
                model.Subject = models.ElementAtOrDefault(i);
                model.Schedule = mContext.Schedules.Where(x => x.Id == model.Subject.Id).ToList();
                listSubject.Add(model);
            }
            return listSubject;
        }
    }
}