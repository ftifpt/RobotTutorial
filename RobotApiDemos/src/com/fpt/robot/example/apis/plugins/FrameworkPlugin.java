package com.fpt.robot.example.apis.plugins;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.fpt.robot.RobotException;
import com.fpt.robot.binder.RobotValue;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.plugins.RobotPlugins;
import com.fpt.robot.ui.RobotActivity;

public class FrameworkPlugin extends RobotActivity implements OnItemClickListener {
	private static final String TAG="FrameworkPlugin";
	
	private static final Integer ACTION_GET_PLUGIN_LIST = 0;
	private static final Integer ACTION_CHECK_PLUGIN_AVAILABLE = 1;
	private static final Integer ACTION_GET_PLUGIN_METHOD_LIST = 2;
	private static final Integer ACTION_CALL_PLUGIN_METHOD = 3;
	
	private static Map<String, Integer> actionMap = null;
	
	private static final String[] actions = new String[] {
		"Get Plugin List",
		"Check Plugin Available",
		"Get Plugin Method List",
		"Call Plugin Method"
	};
	
	private static final Integer[] action_ids = new Integer[] {
		ACTION_GET_PLUGIN_LIST,
		ACTION_CHECK_PLUGIN_AVAILABLE,
		ACTION_GET_PLUGIN_METHOD_LIST,
		ACTION_CALL_PLUGIN_METHOD
	};
	
	private ListView lvActionList;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_framework_plugin);
        
        actionMap = new HashMap<String, Integer>();
        if (actions.length == action_ids.length) {
        	for (int i = 0; i < actions.length; i++) {
        		actionMap.put(actions[i], action_ids[i]);
        	}
        	
        	lvActionList = (ListView) findViewById(R.id.lvActionList);
            
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
            		android.R.layout.simple_list_item_1, actions);
            
            lvActionList.setAdapter(adapter);
            lvActionList.setOnItemClickListener(this);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.framework_plugin, menu);
        return true;
    }
    
    @Override
    protected int getLayoutID() {
        return R.layout.activity_framework_plugin;
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
		Log.d(TAG, "onItemClick(position=" + pos + ")");
		if (pos > actions.length) {
			Log.e(TAG, "out of range!");
			return;
		}
		String module = actions[pos];
		Log.d(TAG, "selected module: '" + module + "'");
		if (actionMap.containsKey(module)) {
			Integer actionId = actionMap.get(module);
			startAction(actionId);
		}
	}

	private void startAction(Integer actionId) {
		if (actionId == ACTION_GET_PLUGIN_LIST) {
			getPluginsList();
		} else if (actionId == ACTION_CHECK_PLUGIN_AVAILABLE) {
			checkIsPluginAvailable();
		} else if (actionId == ACTION_GET_PLUGIN_METHOD_LIST) {
			getPluginMethodList();
		} else if (actionId == ACTION_CALL_PLUGIN_METHOD) {
			callPluginMethod();
		}
	}

	private String[] pluginsList = null;
	private int selectedPluginIndex = 0;
	
	private String[] pluginMethodList = null;
	private int selectedPluginMethodIndex = 0;
	
	private void getPluginsList() {
		new Thread(new Runnable() {
			@Override
			public void run() {
			    showProgress("getting plugins list...");
				try {
					pluginsList = RobotPlugins.getPluginsList(getRobot());
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("get plugins list failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (pluginsList == null && pluginsList.length==0) {
					makeToast("none plugin available!");
					return;
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showInfoListDialog("All plugins", pluginsList, 
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// do nothing
							}
						});
					}
				});
			}
		}).start();
	}

	private void checkIsPluginAvailable() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Check plugin available");
		builder.setMessage("Input plugin name:");
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setText("RobotTestPlugin");
		builder.setView(input);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String pluginName = input.getText().toString();
				doCheckPluginIsAvailable(pluginName);
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	protected void doCheckPluginIsAvailable(final String pluginName) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result = false;
				showProgress("checking plugin's available...");
				try {
					result = RobotPlugins.isPluginAvailable(getRobot(), pluginName);
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("check plugin available failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				String message;
				if (result) {
					message = new String("Plugin '" + pluginName + "' is available!");
				} else {
					message = new String("Plugin '" + pluginName + "' is not available!");
				}
				showInfoDialogFromThread("Result", message);
			}
		}).start();
	}

	private void getPluginMethodList() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Get plugin method list");
		builder.setMessage("Input plugin name:");
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setText("RobotTestPlugin");
		builder.setView(input);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String pluginName = input.getText().toString();
				doGetPluginMethodList(pluginName);
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	protected void doGetPluginMethodList(final String pluginName) {
		new Thread(new Runnable() {
			@Override
			public void run() {
			    showProgress("getting plugin's method list...");
				try {
					pluginMethodList = RobotPlugins.getPluginMethodList(getRobot(), pluginName);
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("get method list for plugin '" + pluginName + "' failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (pluginMethodList == null && pluginMethodList.length==0) {
					makeToast("none methods for plugin '" + pluginName + "' available!");
					return;
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showInfoListDialog("All methods", pluginMethodList, 
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// do nothing
							}
						});
					}
				});
			}
		}).start();
	}

	private void callPluginMethod() {
		new Thread(new Runnable() {
			@Override
			public void run() {
			    showProgress("getting plugins list...");
				try {
					pluginsList = RobotPlugins.getPluginsList(getRobot());
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("get plugins list failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (pluginsList == null && pluginsList.length==0) {
					makeToast("none plugin available!");
					return;
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showSingleChoiceDialog("Choose plugin", pluginsList, 0, 
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								selectedPluginIndex = which;
							}
						}, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								doCallPluginMethod_prepare();
							}
						});
					}
				});
			}
		}).start();
	}
	

	protected void doCallPluginMethod_prepare() {
		if (selectedPluginIndex > pluginsList.length) {
			return;
		}
		final String pluginName = pluginsList[selectedPluginIndex];
		new Thread(new Runnable() {
			@Override
			public void run() {
			    showProgress("getting plugin's method list...");
				try {
					pluginMethodList = RobotPlugins.getPluginMethodList(getRobot(), pluginName);
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("get method list for plugin '" + pluginName + "' failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (pluginMethodList == null && pluginMethodList.length==0) {
					makeToast("none methods for plugin '" + pluginName + "' available!");
					return;
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showSingleChoiceDialog("Choose method", pluginMethodList, 0, 
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int item) {
								selectedPluginMethodIndex = item;
							}
						}, 
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								doCallPluginMethod(pluginName);						
							}
						});
					}
				});
			}
		}).start();
	}

	protected void doCallPluginMethod(final String pluginName) {
		if (selectedPluginMethodIndex > pluginMethodList.length) {
			return;
		}
		final String methodName = pluginMethodList[selectedPluginMethodIndex];
		new Thread(new Runnable() {			
			@Override
			public void run() {
				if (pluginName.equals("RobotTestPlugin") == false) {
					return;
				}
				int methodId = 0;
				if (methodName.equals("test")) {
					methodId = 1;
				} else if (methodName.equals("testSaySomeThing")) {
					methodId = 2;
				} else if (methodName.equals("testSay")) {
					methodId = 3;
				} else {
					return;
				}
				if (methodId != 3) {
					RobotValue argsValue = new RobotValue();
					RobotValue returnValue;
					showProgress("calling plugin's method...");
					try {
						returnValue = RobotPlugins.callPluginMethod(getRobot(), 
								pluginName, methodId, argsValue);
					} catch (final RobotException e) {
						e.printStackTrace();
						cancelProgress();
						makeToast("call method '" + methodName + "' of plugin '" 
									+ pluginName + "' failed! " + e.getMessage());
						return;
					}
					cancelProgress();
					makeToast("return value: " + returnValue.toString());
				} else {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							doGetArgumentMethod3();
						}
					});					
				}				
			}
		}).start();
	}
	
	protected void doGetArgumentMethod3() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Text to say");
		builder.setMessage("Input text to say:");
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setText("I'm here!");
		builder.setView(input);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String textToSay = input.getText().toString();
				doCallMethod3(textToSay);
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	protected void doCallMethod3(final String textToSay) {
		new Thread(new Runnable() {			
			@Override
			public void run() {
				RobotValue argsValue = new RobotValue();
				argsValue.putString(textToSay);
				RobotValue returnValue;
				showProgress("calling plugin's method...");
				try {
					returnValue = RobotPlugins.callPluginMethod(getRobot(), 
							"RobotTestPlugin", 3, argsValue);
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("call method 'testSay' of plugin " +
							"'RobotTestPlugin' failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				makeToast("return value: " + returnValue.toString());
			}
		}).start();
	}

	protected void makeToast(final String toast) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(FrameworkPlugin.this, toast, 
						Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	protected void showAlertDialog(String title, String message, 
			View customView, DialogInterface.OnClickListener positiveListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (title != null && !title.isEmpty()) {
			builder.setTitle(title);
		}
		if (message != null && !message.isEmpty()) {
			builder.setMessage(message);
		}
		if (customView != null) {
			builder.setView(customView);
		}
		builder.setPositiveButton("OK", positiveListener);
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}
	
	protected void showInfoListDialog(String title, CharSequence[] items,
			DialogInterface.OnClickListener choiceListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (title != null && !title.isEmpty()) {
			builder.setTitle(title);
		}
		builder.setItems(items, choiceListener);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}
	
	protected void showInfoDialog(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (title != null && !title.isEmpty()) {
			builder.setTitle(title);
		}
		if (message != null && !message.isEmpty()) {
			builder.setMessage(message);
		}
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}
	
	protected void showInfoDialogFromThread(final String title, final String message) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showInfoDialog(title, message);
			}
		});
	}
	
	protected void showInputDialog(String title, String message,
			DialogInterface.OnClickListener positiveListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (title != null && !title.isEmpty()) {
			builder.setTitle(title);
		}
		if (message != null && !message.isEmpty()) {
			builder.setMessage(message);
		}
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		builder.setView(input);
		builder.setPositiveButton("OK", positiveListener);
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}
	
	protected void showSingleChoiceDialog(String title, CharSequence[] items, int checkedItem,
			DialogInterface.OnClickListener choiceListener,
			DialogInterface.OnClickListener positiveListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (title != null && !title.isEmpty()) {
			builder.setTitle(title);
		}
		builder.setSingleChoiceItems(items, checkedItem, choiceListener);
		builder.setPositiveButton("OK", positiveListener);
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}

    private ProgressDialog progressDialog = null;
    
    protected void showProgress(final String message) {
        //Log.d(TAG, "showProgress('" +message+ "')");
        runOnUiThread(new Runnable() {
            public void run() {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(FrameworkPlugin.this);
                }
                // no title
                if (message != null) {
                    progressDialog.setMessage(message);
                }
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(true);
                progressDialog.show();
            }
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    protected void cancelProgress() {
        //Log.d(TAG, "cancelProgress()");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    //progressDialog.cancel();
                    progressDialog.dismiss();
                }
            }
        });
    }
}
