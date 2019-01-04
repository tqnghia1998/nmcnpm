using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using DbModel;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;

namespace ServerApp.Controllers
{
    public class ExerciseController : Controller
    {
        #region Protected Members

        /// <summary>
        /// Database của server
        /// </summary>
        protected ApplicationDbContext mContext { set; get; }

        /// <summary>
        /// The manager for handling user creation, deletion, searching, roles etc...
        /// </summary>
        protected UserManager<ApplicationUser> mUserManager;

        /// <summary>
        /// The manager for handling signing in and out for our users
        /// </summary>
        protected SignInManager<ApplicationUser> mSignInManager;

        #endregion

        #region Constructor

        /// <summary>
        /// Default constructor
        /// </summary>
        /// <param name="context">The injected context</param>
        /// <param name="signInManager">The Identity sign in manager</param>
        /// <param name="userManager">The Identity user manager</param>
        public ExerciseController(ApplicationDbContext context,
            UserManager<ApplicationUser> userManager,
            SignInManager<ApplicationUser> signInManager)
        {
            mContext = context;
            mUserManager = userManager;
            mSignInManager = signInManager;
        }

        #endregion

        /// <summary>
        /// Gửi yêu cầu tạo bài tập mới
        /// </summary>
        /// <param name="data">Trong data này không chứa Id, Id sẽ tự phát sinh trong hàm</param>
        /// <returns></returns>
        [Route("api/exercise")]
        [HttpPost]
        public IActionResult PostSubject([FromBody] ExerciseDataModel data)
        {
            // Tự động generate Id
            if (data.Id.Equals("auto"))
            {
                int count = mContext.Exercise.Where(x => x.Id.Contains(data.Mssv)).ToList().Count() + 1;
                while (true)
                {
                    data.Id = data.Mssv + "_" + count;
                    if (mContext.Exercise.Find(data.Mssv, data.Id) == null) break;
                    count++;
                }
            }
            try
            {
                mContext.Exercise.Add(data);
                mContext.SaveChanges();
            }
            catch (Exception ex)
            {
                return BadRequest("Tên bài tập đã bị trùng");
            }

            return Ok(new { response = "Tạo bài tập thành công" });
        }

        /// <summary>
        /// Gửi yêu cầu cập nhật bài tập
        /// </summary>
        /// <param name="data">Trong data này chứa Id của một bài tập đã có</param>
        /// <returns></returns>
        [Route("api/exercise/put")]
        [HttpPut("{id}")]
        public IActionResult UpdateSubject([FromBody] ExerciseDataModel data)
        {
            try
            {
                mContext.Entry(data).State = Microsoft.EntityFrameworkCore.EntityState.Modified;
                mContext.SaveChanges();
            }
            catch (Exception ex)
            {
                return BadRequest("Tên bài tập đã bị trùng");
            }

            return Ok(new { response = "Sửa bài tập thành công" });
        }

        /// <summary>
        /// Gửi yêu cầu cập nhật tiến độ
        /// </summary>
        /// <param name="progress"></param>
        /// <returns></returns>
        [Route("api/exercise/put/{id}")]
        [HttpPut("{id}")]
        public IActionResult UpdateSubject(string id, string progress)
        {
            try
            {
                var data = mContext.Exercise.Where(x => x.Id == id).FirstOrDefault();
                data.Progress = progress;
                mContext.Entry(data).State = Microsoft.EntityFrameworkCore.EntityState.Modified;
                mContext.SaveChangesAsync();
            }
            catch (Exception ex)
            {
                return BadRequest("Không thể sửa bài tập này");
            }

            return Ok(new { response = "Sửa bài tập thành công" });
        }


        /// <summary>
        /// Gửi yêu cầu xóa dữ liệu bài tập
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [Route("api/exercise/{id}")]
        [HttpDelete]
        public IActionResult DeleteExercise(string id)
        {
            try
            {
                var db = mContext.Exercise.Where(x => x.Id == id).FirstOrDefault();
                mContext.Exercise.Remove(db);
                mContext.SaveChanges();
            }
            catch (Exception ex)
            {
                return BadRequest("Xóa bài tập thất bại");
            }
            return Ok(new { response = "Xóa bài tập thành công" });
        }

        /// <summary>
        /// Gửi yêu cầu lấy tất cả các bài tập
        /// </summary>
        /// <returns></returns>
        [Route("api/exercise/{mssv}")]
        [HttpGet]
        public List<ExerciseDataModel> GetExercise(string mssv)
        {
            var listExercise = new List<ExerciseDataModel>();

            List<ExerciseDataModel> exercises = mContext.Exercise.Where(x => x.Mssv == mssv).ToList();

            for (int i = 0; i < exercises.Count; i++)
            {
                listExercise.Add(exercises[i]);
            }

            return listExercise;
        }
    }
}