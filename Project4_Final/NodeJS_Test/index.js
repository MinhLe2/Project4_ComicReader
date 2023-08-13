var crypto = require('crypto');
var express = require('express');
var mysql = require('mysql');
var bodyParser = require('body-parser');
const SALT = "I have a dream"
//connect to MySQL
var con = mysql.createConnection({
	host:'localhost',
	user:'root',
	password:'',
	database:'testmanga'
})

//Password util


const sha512 = function(password){
	var hash = crypto.createHmac('sha512',SALT);
	hash.update(password)
	return  hash.digest('hex')	
}
/*
function saltHashPassword(userPassword){
	var salt = genRandomString(16);
	var passwordData = sha512(userPassword, salt);
	return passwordData;
}
var genRandomString = function(length){
	return crypto.randomBytes(Math.ceil(length/2))
	.toString('hex')
	.slice(0,length);
}

*/
/*
function checkHashPassword(userPassword, salt)
{
	var passwordData = sha512(userPassword,salt);
	return passwordData;
}
*/

//create restful
var app=express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended : true}));
var publicDir=(__dirname+'/public/');
app.use(express.static(publicDir));

//UPDATE PASSWORD
app.post('/updatePassword/',(req,res,next)=>{
    const {email = '', password = ''} = req.body
    const  hashedPassword = sha512(password);// get hash value

    con.query('SELECT * FROM user WHERE email = ?',
        [email],function(err, results, fields){
            debugger
            con.on('error',function(err){
                console.log('[MySQL ERROR]', err);
            });

            if(results?.length > 0) {
                con.query('UPDATE user SET password = ? WHERE email = ?',
                    [hashedPassword, email],function(err, result, fields){
                        debugger
                        con.on('error',function(err){
                            console.log('[MySQL ERROR]', err);
                        });
                        res.json('Email send Succesfully');
                    });
            } 
            else
            {
                res.json('Email Does Not Exist');
            }
        });
})
//////////////////////////
app.post('/changePassword',(req,res,next)=>{
    const {email = '',password = '', newPassword = ''} = req.body
    const hashedPassword = sha512(password);
    const newHashedPassword = sha512(newPassword);
    con.query('SELECT * FROM user WHERE email = ? AND password = ?',
        [email,hashedPassword],function(err, results, fields){
            debugger
            con.on('error',function(err){
                console.log('[MySQL ERROR]', err);
            });
            if(results?.length > 0) {
                con.query('UPDATE user SET password = ? WHERE email = ?',
                    [newHashedPassword, email],function(err, result, fields){
                        debugger
                        con.on('error',function(err){
                            console.log('[MySQL ERROR]', err);
                        });
                        if(results?.length > 0) {
                            res.json('Update Successful');
                        } 
                        else
                        {
                            res.json('Wrong Password !');
                        }

                    });
            } 
            else
            {
                res.json('Please input correct Email and Password');
            }
        });


})
	
//LOGIN
app.post('/login/',(req,res,next)=>{
    const {name = '', password = ''} = req.body
    const  hashedPassword = sha512(password);// get hash value

    con.query('SELECT * FROM user WHERE name = ? AND password = ?',
        [name, hashedPassword],function(err, result, fields){
            debugger
            con.on('error',function(err){
                console.log('[MySQL ERROR]', err);
            });

            if(result?.length > 0) {
                res.json('Login Successful')
            } 
            else
            {
                res.json('Wrong username, password');
            }
        });


})

//REGISTER 
// app.get('/',(req,res,next)=>{
// 	res.json("get okjktrijtir")
// })
app.post('/registers/',(req,res,next)=>{	
	const {name = '', password = '', email=''} = req.body		
	const hashedPassword = sha512(password)// get hash value

	con.query('SELECT * FROM `user` WHERE name = ? OR email = ?',[name,email], function(err, result, fields){
		
		con.on('error',function(err){
			console.log('[MySQL ERROR]', err);		
		});
		if(result?.length > 0) {
			res.json('User name or email already exits !!');
			return
		}	
			
		con.query('INSERT INTO `user`( `name`, `email`, `password`) VALUES (?,?,?)'
			,[name,email,hashedPassword], function(err,result,fields){	
				debugger
			con.on('error', function(err){
				console.log('[MySQL ERROR]', err);
				res.json('Register error',err);
			});
				res.json('Register Successful');
			})
	});

})

//INSERT MANGA
app.post('/addmanga/',(req,res,next)=>{	
	const {name = '', image = ''} = req.body		

	con.query('SELECT * FROM `manga` WHERE Name=?',[name], function(err, result, fields){
		
		con.on('error',function(err){
			console.log('[MySQL ERROR]', err);
		});
		if(result?.length > 0) {
			res.json('Manga already exits !!');
			return
		}			
		con.query('INSERT INTO `manga`( `Name`, `Image`) VALUES (?,?)'
			,[name,image], function(err,result,fields){	
				debugger
			con.on('error', function(err){
				console.log('[MySQL ERROR]', err);
				res.json('Insert Error!',err);
			});
				res.json('Insert Manga Successful');
			})
	});

})

//UPDATE MANGA
app.post('/updatemanga/',(req,res,next)=>{	
	const {id = '', name = '', image = ''} = req.body		
	debugger

	// con.query('SELECT * FROM `manga` WHERE name=?',[name], function(err, result, fields){
		
	// 	con.on('error',function(err){
	// 		console.log('[MySQL ERROR]', err);	
	// 	});

	// 	if(result?.length > 0) {
	// 		res.json('Manga already exits !!');
	// 		return
	// 	}		
		con.query('UPDATE `manga` SET `name` = ? , `image` = ? WHERE `id` = ?'
			,[name,image,id], function(err,result,fields){					
				debugger
			con.on('error', function(err){
				console.log('[MySQL ERROR]', err);
				res.json('Update Error!',err);
			});
				res.json('Update Manga Successful');
			})
	// });

})

//DELETE MANGA
app.post('/deletemanga/',(req,res,next)=>{	
	const {id = ''} = req.body		

	// con.query('SELECT * FROM `manga` WHERE name=?',[name], function(err, result, fields){
		
	// 	con.on('error',function(err){
	// 		console.log('[MySQL ERROR]', err);	
	// 	});

	// 	if(result?.length > 0) {
	// 		res.json('Manga already exits !!');
	// 		return
	// 	}		

		con.query('DELETE FROM `manga` WHERE `id` = ?'
			,[id], function(err,result,fields){	
				debugger
			con.on('error', function(err){
				console.log('[MySQL ERROR]', err);
				res.json('Delete Error!',err);
			});
				res.json('Delete Manga Successful');
			})
	// });

})

//INSERT CHAPTER
app.post('/addchapter/',(req,res,next)=>{	
	const {name = '', mangaid = ''} = req.body		

	con.query('SELECT * FROM `chapter` WHERE Name=?',[name], function(err, result, fields){
		
		con.on('error',function(err){
			console.log('[MySQL ERROR]', err);
		});
		if(result?.length > 0) {
			res.json('Chapter already exits !!');
			return
		}			
		con.query('INSERT INTO `chapter`( `Name`, `MangaID`) VALUES (?,?)'
			,[name,mangaid], function(err,result,fields){	
				debugger
			con.on('error', function(err){
				console.log('[MySQL ERROR]', err);
				res.json('Insert Error!',err);
			});
				res.json('Insert Chapter Successful');
			})
	});

})

//UPDATE CHAPTER
app.post('/updatechapter/',(req,res,next)=>{	
	const {id = '', name = '', mangaid = ''} = req.body		

	// con.query('SELECT * FROM `chapter` WHERE Name=?',[name], function(err, result, fields){
		
	// 	con.on('error',function(err){
	// 		console.log('[MySQL ERROR]', err);	
	// 	});

	// 	if(result?.length > 0) {
	// 		res.json('Manga already exits !!');
	// 		return
	// 	}		

		con.query('UPDATE `chapter` SET `Name` = ? , `MangaID` = ? WHERE `ID` = ?'
			,[name,mangaid,id], function(err,result,fields){	
				debugger
			con.on('error', function(err){
				console.log('[MySQL ERROR]', err);
				res.json('Update Error!',err);
			});
				res.json('Update Chapter Successful');
			})
	// });

})

//DELETE CHAPTER
app.post('/deletechapter/',(req,res,next)=>{	
	const {id = ''} = req.body		

	// con.query('SELECT * FROM `chapter` WHERE id=?',[id], function(err, result, fields){
		
	// 	con.on('error',function(err){
	// 		console.log('[MySQL ERROR]', err);	
	// 	});

	// 	if(result?.length > 0) {
	// 		res.json('Manga already exits !!');
	// 		return
	// 	}		

		con.query('DELETE FROM `chapter` WHERE `id` = ?'
			,[id], function(err,result,fields){	
				debugger
			con.on('error', function(err){
				console.log('[MySQL ERROR]', err);
				res.json('Delete Error!',err);
			});
				res.json('Delete Chapter Successful');
			})
	// });

})

//INSERT LINK
app.post('/addlink/',(req,res,next)=>{	
	const {link = '', chapterid = ''} = req.body		

	con.query('SELECT * FROM `link` WHERE Link=?',[link], function(err, result, fields){
		
		con.on('error',function(err){
			console.log('[MySQL ERROR]', err);
		});

		if(result?.length > 0) {
			res.json('This Link picture already exits !!');
			return
		}	

		con.query('INSERT INTO `link`( `Link`, `ChapterID`) VALUES (?,?)'
			,[link,chapterid], function(err,result,fields){	
				debugger
			con.on('error', function(err){
				console.log('[MySQL ERROR]', err);
				res.json('Insert Error!',err);
			});
				res.json('Insert Link Successful');
			})
	});

})

//UPDATE LINK
app.post('/updatelink/',(req,res,next)=>{	
	const {id = '', link = '', chapterid = ''} = req.body		
	debugger
	// con.query('SELECT * FROM `chapter` WHERE link=?',[link], function(err, result, fields){
		
	// 	con.on('error',function(err){
	// 		console.log('[MySQL ERROR]', err);	
	// 	});

	// 	if(result?.length > 0) {
	// 		res.json('Manga already exits !!');
	// 		return
	// 	}		

		con.query('UPDATE `link` SET `Link` = ? , `ChapterID` = ? WHERE `ID` = ?'
			,[link,chapterid,id], function(err,result,fields){	
				debugger
				if(!err) {
						res.json('Update link Successful!!!!!!!!!');		
					} else {
						res.json('Update Error!',err);	
					}			
	 	});

})

//DELETE LINK
app.post('/deletelink/',(req,res,next)=>{	
	const {id = ''} = req.body		

	// con.query('SELECT * FROM `manga` WHERE name=?',[name], function(err, result, fields){
		
	// 	con.on('error',function(err){
	// 		console.log('[MySQL ERROR]', err);	
	// 	});

	// 	if(result?.length > 0) {
	// 		res.json('Manga already exits !!');
	// 		return
	// 	}		

		con.query('DELETE FROM `link` WHERE `id` = ?'
			,[id], function(err,result,fields){	
				debugger
			con.on('error', function(err){
				console.log('[MySQL ERROR]', err);
				res.json('Delete Error!',err);
			});
				res.json('Delete link picture Successful!');
			})
	// });

})


//INSERT MangaCategory

app.post('/addMangaCategory/',(req,res,next) => {	
	debugger
	const {mangaid = '', categoryid = ''} = req.body		
	con.query('INSERT INTO `mangacategory`( `MangaID`, `CategoryID`) VALUES (?,?)'
			,[mangaid,categoryid], 
			(err,result,fields) => {	
				debugger
					console.log('[MySQL ERROR]', err);
					if(!err) {
						res.json('Insert Category Successful!!!!!!!!!');		
					} else {
						res.json('Insert Error!',err);	
					}
				
			}
	)
})


//UPDATE MangaCategory
app.post('/updatemangacategory/',(req,res,next)=>{	
	const {mangaid = '', categoryid = '',id =''} = req.body		
		con.query('UPDATE `mangacategory` SET `MangaID` = ? , `CategoryID` = ? WHERE `ID` = ?'
			,[mangaid,categoryid,id], 
			(err,result,fields) => {	
				debugger
					console.log('[MySQL ERROR]', err);
					if(!err) {
						res.json('UPDATE Category Successful!!!!!!!!!');		
					} else {
						res.json('UPDATE Error!',err);	
					}
				
			}
	// });
	)

})

//DELETE MangaCategory
app.post('/deletemangacategory/',(req,res,next)=>{	
	const {id} = req.body		
		con.query('DELETE FROM `mangacategory` WHERE `ID` = ?'
			,[id], 
			(err,result,fields) => {	
				debugger
					console.log('[MySQL ERROR]', err);
					if(!err) {
						res.json('DELETE Category Successful!!!!!!!!!');		
					} else {
						res.json('DELETE Error!',err);	
					}
			}
	// });
	)

})




//TEST

// app.get("/",(req, res, next)=> {
// 	console.log('Password: 123456');
// 	var encrypt = saltHashPassword("123456");
// 	console.log('Encrypt: '+encrypt.passwordHash);
// 	console.log('Salt: '+encrypt.salt);
	
// })

///////////////////////////////////////////////////////////////////////////////////////

require('events').EventEmitter.defaultMaxListerners = 20;// khac phuc memory leaked

//GET ALL BANNER
 app.get("/banner",(req,res,next)=>{
	con.query('SELECT * FROM banner',function(error,result,fields){
		con.on('error',function(err){
				console.log('[MY SQL ERROR]',err);
		});
	if (result && result.length) {
		res.end(JSON.stringify(result));
	}
	else{
		res.end(JSON.stringify("No banner Here"));
	}
	})
	
});

//GET ALL MangaCategory
app.get("/mangacategory",(req,res,next)=>{
	con.query('SELECT * FROM mangacategory',function(error,result,fields){
		con.on('error',function(err){
				console.log('[MYSQL ERROR]',err);
		});
	if (result && result.length) {
		res.end(JSON.stringify(result))
	}
	else{
		res.end(JSON.stringify("No mCategory Here"));
	}
	})
});

//GET ALL Comic
app.get("/comic",(req,res,next)=>{
	con.query('SELECT * FROM manga',function(error,result,fields){
		con.on('error',function(err){
				console.log('[MYSQL ERROR]',err);
		});
	if (result && result.length) {
		res.end(JSON.stringify(result))
	}
	else{
		res.end(JSON.stringify("No Comic Here"));
	}
	})
});

app.get("/link",(req,res,next)=>{
	con.query('SELECT * FROM link',function(error,result,fields){
		con.on('error',function(err){
				console.log('[MYSQL ERROR]',err);
		});
	if (result && result.length) {
		res.end(JSON.stringify(result))
		return
	}
	else{
		res.end(JSON.stringify("No Link Here"));
	}
	})
});

//GET ALL CHAPTER
app.get("/chapter",(req,res,next)=>{
	con.query('SELECT * FROM chapter',function(error,result,fields){
		con.on('error',function(err){
				console.log('[MY SQL ERROR]',err);
		});
	if (result && result.length) {
		res.end(JSON.stringify(result));
	}
	else{
		res.end(JSON.stringify("No chapter Here"));
	}
	})
	
});

//GET CHAPER BY MANGA ID
app.get("/chapter/:mangaid",(req,res,next)=>{
	con.query('SELECT * FROM chapter where MangaID=?',[req.params.mangaid],function(error,result,fields){
		con.on('error',function(err){
				console.log('[MY SQL ERROR]',err);
		});
	if (result && result.length) {
		res.end(JSON.stringify(result))
	}
	else{
		res.end(JSON.stringify("No chapter Here"));
	}
	})
});


//GET IMAGE BY CHAPTER ID
app.get("/links/:chapterid",(req,res,next)=>{
	con.query('SELECT * FROM link where ChapterId=?',[req.params.chapterid],function(error,result,fields){
		con.on('error',function(err){
				console.log('[MYSQL ERROR]',err);
		});
	if (result && result.length) {
		res.end(JSON.stringify(result))
	}
	else{
		res.end(JSON.stringify("No Comic Here"));
	}
	})
});

//GET ALL Category
app.get("/categories",(req,res,next)=>{
	con.query('SELECT * FROM Category',function(error,result,fields){
		con.on('error',function(err){
				console.log('[MY SQL ERROR]',err);
		});
		if (result && result.length)
		{
			res.end(JSON.stringify(result))
		}
		else
		{
			res.end(JSON.stringify("No category Here"));
		}
	})
});


//GET ALL FILTER
app.post("/filter",(req,res,next)=>{
	var post_data =req.body;//Get post data from post request
	var array = JSON.parse(post_data.data); //Parse 'data' field from Post request to Json array
	var query = "SELECT * FROM manga WHERE ID IN (SELECT MangaID FROM mangacategory";//default query
	if (array.length > 0) {
		query+=" GROUP BY MangaID";
		if (array.length == 1) //if user just submit 1 category
			query+=" HAVING SUM(CASE WHEN CategoryID ="+array[0]+" THEN 1 ELSE 0 END) > 0 )";
		else {//if user submit more than 1 category
			for(var i = 0 ; i< array.length;i++){
				if(i == 0)//first condition
					query+=" HAVING SUM(CASE WHEN CategoryID ="+array[0]+" THEN 1 ELSE 0 END) > 0 AND ";
				else if(i == array.length-1)// Last condition
					query+=" SUM(CASE WHEN CategoryID = "+array[i]+" THEN 1 ELSE 0 END) > 0)";
				else
					query+=" SUM(CASE WHEN CategoryID = "+array[i]+" THEN 1 ELSE 0 END) > 0 AND";

			}
		}
		con.query(query,function(error,result,fields){
		con.on('error',function(err){
				console.log('[MY SQL ERROR]',err);
		});
		if (result && result.length)
		{
			res.end(JSON.stringify(result))
		}
		else
		{
			res.end(JSON.stringify("No comic Here"));
		}
	})
	}
});


//SEARCH MANGA BY NAME
app.post("/search",(req,res,next)=>{
	var post_data = req.body; //get body post
	var name_search = post_data.search;//get 'Search' data from POST REQUEST

	var query = "SELECT * FROM manga WHERE Name LIKE '%"+name_search+"%'";

	con.query(query,function(error,result,fields){
		con.on('error',function(err){
				console.log('[MYSQL ERROR]',err);
		});
	if (result && result.length) {
		res.end(JSON.stringify(result))
	}
	else{
		res.end(JSON.stringify("No comic Here"));
	}
	})

});





//start server 
app.listen(3000, ()=>{
	console.log('project is running on port 3000');
})
