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
                return BadRequest("Đăng ký môn học thất bại");
            }
            return Ok(new { response = "Đăng ký môn học thành công" });
        }

        /// <summary>
        /// Gửi yêu cầu xóa dữ liệu đăng ký
        /// </summary>
        /// <param name="mssv"></param>
        /// <param name="id"></param>
        /// <param name="dayInTheWeek"></param>
        /// <returns></returns>
        [Route("api/registered/{mssv}/{id}/{dayInTheWeek}")]
        [HttpDelete]
        public IActionResult DeleteRegistered(string mssv, string id, string dayInTheWeek)
        {
            try
            {
                var db = mContext.Registered.FirstOrDefault(x => x.Mssv == mssv 
                                                        && x.Id == id 
                                                        && x.DayInTheWeek == dayInTheWeek);
                if (db != null)
                {
                    mContext.Registered.Remove(db);
                    mContext.SaveChanges();
                }
            }
            catch (Exception ex)
            {
                return BadRequest("Hủy đăng ký môn học thất bại");
            }
            return Ok(new { response = "Hủy đăng ký môn học thành công" });
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
        public CreateSubjectCredentialsDataModel GetSubjectById(string subject)
        {
            var model = new CreateSubjectCredentialsDataModel();
            // FirstOrDefault() có nghĩa: Nếu có thì lấy phần tử đầu, không thì null
            model.Subject = mContext.Subject.Where(x => x.Subject == subject).FirstOrDefault();
            // Lấy danh sách lịch học tương ứng
            model.Schedule = mContext.Schedules.Where(x => x.Id == model.Subject.Id).ToList();
            return model;
        }

        /// <summary>
        /// Lấy danh sách tên môn học theo các lựa chọn
        /// </summary>
        /// <param name="major"></param>
        /// <param name="course"></param>
        /// <param name="term"></param>
        /// <param name="mssv"></param>
        /// <param name="status"></param>
        /// <returns></returns>
        [HttpGet("api/subject/{major}/{course}/{term}/{mssv}/{status}")]
        public List<SubjectDTO> GetSubjectDTO(string major
                                               , string course
                                                , string term
                                                 , string mssv
                                                  , int status)
        {
            // Tạo danh sách các data transfer object môn học
            var listSubjectDTO = new List<SubjectDTO>();

            // Lấy danh sách các môn học tương ứng
            List<SubjectDataModel> subjects = mContext.Subject.Where(x => EF.Functions.Like(x.Major, major)
                                                                     && EF.Functions.Like(x.Course, course)
                                                                     && EF.Functions.Like(x.Term, term)
                                                                     && x.Status == status).ToList()
;            // Đưa những dữ liệu thực sự cần thiết vào DTO
            for (int i = 0; i < subjects.Count(); i++)
            {
                var model = new SubjectDTO();

                // Lấy tên môn học
                model.Id = subjects.ElementAtOrDefault(i).Id;
                model.Subject = subjects.ElementAtOrDefault(i).Subject;

                // Kiểm tra id môn học có trong bảng Registered hay không
                if (mContext.Registered.Where(x => x.Mssv == mssv
                                                && x.Id == subjects.ElementAtOrDefault(i).Id).Count() > 0)
                {
                    model.isRegistered = true;
                }
                else
                {
                    model.isRegistered = false;
                }
                listSubjectDTO.Add(model);
               
            }
            return listSubjectDTO;
        }
    }
}