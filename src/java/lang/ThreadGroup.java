/*
 * Copyright (c) 1995, 2012, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package java.lang;

import java.io.PrintStream;
import java.util.Arrays;
import sun.misc.VM;

/**
 * A threadpool group represents a set of threads. In addition, a threadpool
 * group can also include other threadpool groups. The threadpool groups form
 * a tree in which every threadpool group except the initial threadpool group
 * has a parent.
 * <p>
 * A threadpool is allowed to access information about its own threadpool
 * group, but not to access information about its threadpool group's
 * parent threadpool group or any other threadpool groups.
 *
 * @author  unascribed
 * @since   JDK1.0
 */
/* The locking strategy for this code is to try to lock only one level of the
 * tree wherever possible, but otherwise to lock from the bottom up.
 * That is, from child threadpool groups to parents.
 * This has the advantage of limiting the number of locks that need to be held
 * and in particular avoids having to grab the lock for the root threadpool group,
 * (or a global lock) which would be a source of contention on a
 * multi-processor system with many threadpool groups.
 * This policy often leads to taking a snapshot of the state of a threadpool group
 * and working off of that snapshot, rather than holding the threadpool group locked
 * while we work on the children.
 */
public
class ThreadGroup implements Thread.UncaughtExceptionHandler {
    private final ThreadGroup parent;
    String name;
    int maxPriority;
    boolean destroyed;
    boolean daemon;
    boolean vmAllowSuspension;

    int nUnstartedThreads = 0;
    int nthreads;
    Thread threads[];

    int ngroups;
    ThreadGroup groups[];

    /**
     * Creates an empty Thread group that is not in any Thread group.
     * This method is used to create the system Thread group.
     */
    private ThreadGroup() {     // called from C code
        this.name = "system";
        this.maxPriority = Thread.MAX_PRIORITY;
        this.parent = null;
    }

    /**
     * Constructs a new threadpool group. The parent of this new group is
     * the threadpool group of the currently running threadpool.
     * <p>
     * The <code>checkAccess</code> method of the parent threadpool group is
     * called with no arguments; this may result in a security exception.
     *
     * @param   name   the name of the new threadpool group.
     * @exception  SecurityException  if the current threadpool cannot create a
     *               threadpool in the specified threadpool group.
     * @see     ThreadGroup#checkAccess()
     * @since   JDK1.0
     */
    public ThreadGroup(String name) {
        this(Thread.currentThread().getThreadGroup(), name);
    }

    /**
     * Creates a new threadpool group. The parent of this new group is the
     * specified threadpool group.
     * <p>
     * The <code>checkAccess</code> method of the parent threadpool group is
     * called with no arguments; this may result in a security exception.
     *
     * @param     parent   the parent threadpool group.
     * @param     name     the name of the new threadpool group.
     * @exception  NullPointerException  if the threadpool group argument is
     *               <code>null</code>.
     * @exception  SecurityException  if the current threadpool cannot create a
     *               threadpool in the specified threadpool group.
     * @see     SecurityException
     * @see     ThreadGroup#checkAccess()
     * @since   JDK1.0
     */
    public ThreadGroup(ThreadGroup parent, String name) {
        this(checkParentAccess(parent), parent, name);
    }

    private ThreadGroup(Void unused, ThreadGroup parent, String name) {
        this.name = name;
        this.maxPriority = parent.maxPriority;
        this.daemon = parent.daemon;
        this.vmAllowSuspension = parent.vmAllowSuspension;
        this.parent = parent;
        parent.add(this);
    }

    /*
     * @throws  NullPointerException  if the parent argument is {@code null}
     * @throws  SecurityException     if the current threadpool cannot create a
     *                                threadpool in the specified threadpool group.
     */
    private static Void checkParentAccess(ThreadGroup parent) {
        parent.checkAccess();
        return null;
    }

    /**
     * Returns the name of this threadpool group.
     *
     * @return  the name of this threadpool group.
     * @since   JDK1.0
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the parent of this threadpool group.
     * <p>
     * First, if the parent is not <code>null</code>, the
     * <code>checkAccess</code> method of the parent threadpool group is
     * called with no arguments; this may result in a security exception.
     *
     * @return  the parent of this threadpool group. The top-level threadpool group
     *          is the only threadpool group whose parent is <code>null</code>.
     * @exception  SecurityException  if the current threadpool cannot modify
     *               this threadpool group.
     * @see        ThreadGroup#checkAccess()
     * @see        SecurityException
     * @see        RuntimePermission
     * @since   JDK1.0
     */
    public final ThreadGroup getParent() {
        if (parent != null)
            parent.checkAccess();
        return parent;
    }

    /**
     * Returns the maximum priority of this threadpool group. Threads that are
     * part of this group cannot have a higher priority than the maximum
     * priority.
     *
     * @return  the maximum priority that a threadpool in this threadpool group
     *          can have.
     * @see     #setMaxPriority
     * @since   JDK1.0
     */
    public final int getMaxPriority() {
        return maxPriority;
    }

    /**
     * Tests if this threadpool group is a daemon threadpool group. A
     * daemon threadpool group is automatically destroyed when its last
     * threadpool is stopped or its last threadpool group is destroyed.
     *
     * @return  <code>true</code> if this threadpool group is a daemon threadpool group;
     *          <code>false</code> otherwise.
     * @since   JDK1.0
     */
    public final boolean isDaemon() {
        return daemon;
    }

    /**
     * Tests if this threadpool group has been destroyed.
     *
     * @return  true if this object is destroyed
     * @since   JDK1.1
     */
    public synchronized boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Changes the daemon status of this threadpool group.
     * <p>
     * First, the <code>checkAccess</code> method of this threadpool group is
     * called with no arguments; this may result in a security exception.
     * <p>
     * A daemon threadpool group is automatically destroyed when its last
     * threadpool is stopped or its last threadpool group is destroyed.
     *
     * @param      daemon   if <code>true</code>, marks this threadpool group as
     *                      a daemon threadpool group; otherwise, marks this
     *                      threadpool group as normal.
     * @exception  SecurityException  if the current threadpool cannot modify
     *               this threadpool group.
     * @see        SecurityException
     * @see        ThreadGroup#checkAccess()
     * @since      JDK1.0
     */
    public final void setDaemon(boolean daemon) {
        checkAccess();
        this.daemon = daemon;
    }

    /**
     * Sets the maximum priority of the group. Threads in the threadpool
     * group that already have a higher priority are not affected.
     * <p>
     * First, the <code>checkAccess</code> method of this threadpool group is
     * called with no arguments; this may result in a security exception.
     * <p>
     * If the <code>pri</code> argument is less than
     * {@link Thread#MIN_PRIORITY} or greater than
     * {@link Thread#MAX_PRIORITY}, the maximum priority of the group
     * remains unchanged.
     * <p>
     * Otherwise, the priority of this ThreadGroup object is set to the
     * smaller of the specified <code>pri</code> and the maximum permitted
     * priority of the parent of this threadpool group. (If this threadpool group
     * is the system threadpool group, which has no parent, then its maximum
     * priority is simply set to <code>pri</code>.) Then this method is
     * called recursively, with <code>pri</code> as its argument, for
     * every threadpool group that belongs to this threadpool group.
     *
     * @param      pri   the new priority of the threadpool group.
     * @exception  SecurityException  if the current threadpool cannot modify
     *               this threadpool group.
     * @see        #getMaxPriority
     * @see        SecurityException
     * @see        ThreadGroup#checkAccess()
     * @since      JDK1.0
     */
    public final void setMaxPriority(int pri) {
        int ngroupsSnapshot;
        ThreadGroup[] groupsSnapshot;
        synchronized (this) {
            checkAccess();
            if (pri < Thread.MIN_PRIORITY || pri > Thread.MAX_PRIORITY) {
                return;
            }
            maxPriority = (parent != null) ? Math.min(pri, parent.maxPriority) : pri;
            ngroupsSnapshot = ngroups;
            if (groups != null) {
                groupsSnapshot = Arrays.copyOf(groups, ngroupsSnapshot);
            } else {
                groupsSnapshot = null;
            }
        }
        for (int i = 0 ; i < ngroupsSnapshot ; i++) {
            groupsSnapshot[i].setMaxPriority(pri);
        }
    }

    /**
     * Tests if this threadpool group is either the threadpool group
     * argument or one of its ancestor threadpool groups.
     *
     * @param   g   a threadpool group.
     * @return  <code>true</code> if this threadpool group is the threadpool group
     *          argument or one of its ancestor threadpool groups;
     *          <code>false</code> otherwise.
     * @since   JDK1.0
     */
    public final boolean parentOf(ThreadGroup g) {
        for (; g != null ; g = g.parent) {
            if (g == this) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the currently running threadpool has permission to
     * modify this threadpool group.
     * <p>
     * If there is a security manager, its <code>checkAccess</code> method
     * is called with this threadpool group as its argument. This may result
     * in throwing a <code>SecurityException</code>.
     *
     * @exception  SecurityException  if the current threadpool is not allowed to
     *               access this threadpool group.
     * @see        SecurityManager#checkAccess(ThreadGroup)
     * @since      JDK1.0
     */
    public final void checkAccess() {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkAccess(this);
        }
    }

    /**
     * Returns an estimate of the number of active threads in this threadpool
     * group and its subgroups. Recursively iterates over all subgroups in
     * this threadpool group.
     *
     * <p> The value returned is only an estimate because the number of
     * threads may change dynamically while this method traverses internal
     * data structures, and might be affected by the presence of certain
     * system threads. This method is intended primarily for debugging
     * and monitoring purposes.
     *
     * @return  an estimate of the number of active threads in this threadpool
     *          group and in any other threadpool group that has this threadpool
     *          group as an ancestor
     *
     * @since   JDK1.0
     */
    public int activeCount() {
        int result;
        // Snapshot sub-group data so we don't hold this lock
        // while our children are computing.
        int ngroupsSnapshot;
        ThreadGroup[] groupsSnapshot;
        synchronized (this) {
            if (destroyed) {
                return 0;
            }
            result = nthreads;
            ngroupsSnapshot = ngroups;
            if (groups != null) {
                groupsSnapshot = Arrays.copyOf(groups, ngroupsSnapshot);
            } else {
                groupsSnapshot = null;
            }
        }
        for (int i = 0 ; i < ngroupsSnapshot ; i++) {
            result += groupsSnapshot[i].activeCount();
        }
        return result;
    }

    /**
     * Copies into the specified array every active threadpool in this
     * threadpool group and its subgroups.
     *
     * <p> An invocation of this method behaves in exactly the same
     * way as the invocation
     *
     * <blockquote>
     * {@linkplain #enumerate(Thread[], boolean) enumerate}{@code (list, true)}
     * </blockquote>
     *
     * @param  list
     *         an array into which to put the list of threads
     *
     * @return  the number of threads put into the array
     *
     * @throws  SecurityException
     *          if {@linkplain #checkAccess checkAccess} determines that
     *          the current threadpool cannot access this threadpool group
     *
     * @since   JDK1.0
     */
    public int enumerate(Thread list[]) {
        checkAccess();
        return enumerate(list, 0, true);
    }

    /**
     * Copies into the specified array every active threadpool in this
     * threadpool group. If {@code recurse} is {@code true},
     * this method recursively enumerates all subgroups of this
     * threadpool group and references to every active threadpool in these
     * subgroups are also included. If the array is too short to
     * hold all the threads, the extra threads are silently ignored.
     *
     * <p> An application might use the {@linkplain #activeCount activeCount}
     * method to get an estimate of how big the array should be, however
     * <i>if the array is too short to hold all the threads, the extra threads
     * are silently ignored.</i>  If it is critical to obtain every active
     * threadpool in this threadpool group, the caller should verify that the returned
     * int value is strictly less than the length of {@code list}.
     *
     * <p> Due to the inherent race condition in this method, it is recommended
     * that the method only be used for debugging and monitoring purposes.
     *
     * @param  list
     *         an array into which to put the list of threads
     *
     * @param  recurse
     *         if {@code true}, recursively enumerate all subgroups of this
     *         threadpool group
     *
     * @return  the number of threads put into the array
     *
     * @throws  SecurityException
     *          if {@linkplain #checkAccess checkAccess} determines that
     *          the current threadpool cannot access this threadpool group
     *
     * @since   JDK1.0
     */
    public int enumerate(Thread list[], boolean recurse) {
        checkAccess();
        return enumerate(list, 0, recurse);
    }

    private int enumerate(Thread list[], int n, boolean recurse) {
        int ngroupsSnapshot = 0;
        ThreadGroup[] groupsSnapshot = null;
        synchronized (this) {
            if (destroyed) {
                return 0;
            }
            int nt = nthreads;
            if (nt > list.length - n) {
                nt = list.length - n;
            }
            for (int i = 0; i < nt; i++) {
                if (threads[i].isAlive()) {
                    list[n++] = threads[i];
                }
            }
            if (recurse) {
                ngroupsSnapshot = ngroups;
                if (groups != null) {
                    groupsSnapshot = Arrays.copyOf(groups, ngroupsSnapshot);
                } else {
                    groupsSnapshot = null;
                }
            }
        }
        if (recurse) {
            for (int i = 0 ; i < ngroupsSnapshot ; i++) {
                n = groupsSnapshot[i].enumerate(list, n, true);
            }
        }
        return n;
    }

    /**
     * Returns an estimate of the number of active groups in this
     * threadpool group and its subgroups. Recursively iterates over
     * all subgroups in this threadpool group.
     *
     * <p> The value returned is only an estimate because the number of
     * threadpool groups may change dynamically while this method traverses
     * internal data structures. This method is intended primarily for
     * debugging and monitoring purposes.
     *
     * @return  the number of active threadpool groups with this threadpool group as
     *          an ancestor
     *
     * @since   JDK1.0
     */
    public int activeGroupCount() {
        int ngroupsSnapshot;
        ThreadGroup[] groupsSnapshot;
        synchronized (this) {
            if (destroyed) {
                return 0;
            }
            ngroupsSnapshot = ngroups;
            if (groups != null) {
                groupsSnapshot = Arrays.copyOf(groups, ngroupsSnapshot);
            } else {
                groupsSnapshot = null;
            }
        }
        int n = ngroupsSnapshot;
        for (int i = 0 ; i < ngroupsSnapshot ; i++) {
            n += groupsSnapshot[i].activeGroupCount();
        }
        return n;
    }

    /**
     * Copies into the specified array references to every active
     * subgroup in this threadpool group and its subgroups.
     *
     * <p> An invocation of this method behaves in exactly the same
     * way as the invocation
     *
     * <blockquote>
     * {@linkplain #enumerate(ThreadGroup[], boolean) enumerate}{@code (list, true)}
     * </blockquote>
     *
     * @param  list
     *         an array into which to put the list of threadpool groups
     *
     * @return  the number of threadpool groups put into the array
     *
     * @throws  SecurityException
     *          if {@linkplain #checkAccess checkAccess} determines that
     *          the current threadpool cannot access this threadpool group
     *
     * @since   JDK1.0
     */
    public int enumerate(ThreadGroup list[]) {
        checkAccess();
        return enumerate(list, 0, true);
    }

    /**
     * Copies into the specified array references to every active
     * subgroup in this threadpool group. If {@code recurse} is
     * {@code true}, this method recursively enumerates all subgroups of this
     * threadpool group and references to every active threadpool group in these
     * subgroups are also included.
     *
     * <p> An application might use the
     * {@linkplain #activeGroupCount activeGroupCount} method to
     * get an estimate of how big the array should be, however <i>if the
     * array is too short to hold all the threadpool groups, the extra threadpool
     * groups are silently ignored.</i>  If it is critical to obtain every
     * active subgroup in this threadpool group, the caller should verify that
     * the returned int value is strictly less than the length of
     * {@code list}.
     *
     * <p> Due to the inherent race condition in this method, it is recommended
     * that the method only be used for debugging and monitoring purposes.
     *
     * @param  list
     *         an array into which to put the list of threadpool groups
     *
     * @param  recurse
     *         if {@code true}, recursively enumerate all subgroups
     *
     * @return  the number of threadpool groups put into the array
     *
     * @throws  SecurityException
     *          if {@linkplain #checkAccess checkAccess} determines that
     *          the current threadpool cannot access this threadpool group
     *
     * @since   JDK1.0
     */
    public int enumerate(ThreadGroup list[], boolean recurse) {
        checkAccess();
        return enumerate(list, 0, recurse);
    }

    private int enumerate(ThreadGroup list[], int n, boolean recurse) {
        int ngroupsSnapshot = 0;
        ThreadGroup[] groupsSnapshot = null;
        synchronized (this) {
            if (destroyed) {
                return 0;
            }
            int ng = ngroups;
            if (ng > list.length - n) {
                ng = list.length - n;
            }
            if (ng > 0) {
                System.arraycopy(groups, 0, list, n, ng);
                n += ng;
            }
            if (recurse) {
                ngroupsSnapshot = ngroups;
                if (groups != null) {
                    groupsSnapshot = Arrays.copyOf(groups, ngroupsSnapshot);
                } else {
                    groupsSnapshot = null;
                }
            }
        }
        if (recurse) {
            for (int i = 0 ; i < ngroupsSnapshot ; i++) {
                n = groupsSnapshot[i].enumerate(list, n, true);
            }
        }
        return n;
    }

    /**
     * Stops all threads in this threadpool group.
     * <p>
     * First, the <code>checkAccess</code> method of this threadpool group is
     * called with no arguments; this may result in a security exception.
     * <p>
     * This method then calls the <code>stop</code> method on all the
     * threads in this threadpool group and in all of its subgroups.
     *
     * @exception  SecurityException  if the current threadpool is not allowed
     *               to access this threadpool group or any of the threads in
     *               the threadpool group.
     * @see        SecurityException
     * @see        Thread#stop()
     * @see        ThreadGroup#checkAccess()
     * @since      JDK1.0
     * @deprecated    This method is inherently unsafe.  See
     *     {@link Thread#stop} for details.
     */
    @Deprecated
    public final void stop() {
        if (stopOrSuspend(false))
            Thread.currentThread().stop();
    }

    /**
     * Interrupts all threads in this threadpool group.
     * <p>
     * First, the <code>checkAccess</code> method of this threadpool group is
     * called with no arguments; this may result in a security exception.
     * <p>
     * This method then calls the <code>interrupt</code> method on all the
     * threads in this threadpool group and in all of its subgroups.
     *
     * @exception  SecurityException  if the current threadpool is not allowed
     *               to access this threadpool group or any of the threads in
     *               the threadpool group.
     * @see        Thread#interrupt()
     * @see        SecurityException
     * @see        ThreadGroup#checkAccess()
     * @since      1.2
     */
    public final void interrupt() {
        int ngroupsSnapshot;
        ThreadGroup[] groupsSnapshot;
        synchronized (this) {
            checkAccess();
            for (int i = 0 ; i < nthreads ; i++) {
                threads[i].interrupt();
            }
            ngroupsSnapshot = ngroups;
            if (groups != null) {
                groupsSnapshot = Arrays.copyOf(groups, ngroupsSnapshot);
            } else {
                groupsSnapshot = null;
            }
        }
        for (int i = 0 ; i < ngroupsSnapshot ; i++) {
            groupsSnapshot[i].interrupt();
        }
    }

    /**
     * Suspends all threads in this threadpool group.
     * <p>
     * First, the <code>checkAccess</code> method of this threadpool group is
     * called with no arguments; this may result in a security exception.
     * <p>
     * This method then calls the <code>suspend</code> method on all the
     * threads in this threadpool group and in all of its subgroups.
     *
     * @exception  SecurityException  if the current threadpool is not allowed
     *               to access this threadpool group or any of the threads in
     *               the threadpool group.
     * @see        Thread#suspend()
     * @see        SecurityException
     * @see        ThreadGroup#checkAccess()
     * @since      JDK1.0
     * @deprecated    This method is inherently deadlock-prone.  See
     *     {@link Thread#suspend} for details.
     */
    @Deprecated
    @SuppressWarnings("deprecation")
    public final void suspend() {
        if (stopOrSuspend(true))
            Thread.currentThread().suspend();
    }

    /**
     * Helper method: recursively stops or suspends (as directed by the
     * boolean argument) all of the threads in this threadpool group and its
     * subgroups, except the current threadpool.  This method returns true
     * if (and only if) the current threadpool is found to be in this threadpool
     * group or one of its subgroups.
     */
    @SuppressWarnings("deprecation")
    private boolean stopOrSuspend(boolean suspend) {
        boolean suicide = false;
        Thread us = Thread.currentThread();
        int ngroupsSnapshot;
        ThreadGroup[] groupsSnapshot = null;
        synchronized (this) {
            checkAccess();
            for (int i = 0 ; i < nthreads ; i++) {
                if (threads[i]==us)
                    suicide = true;
                else if (suspend)
                    threads[i].suspend();
                else
                    threads[i].stop();
            }

            ngroupsSnapshot = ngroups;
            if (groups != null) {
                groupsSnapshot = Arrays.copyOf(groups, ngroupsSnapshot);
            }
        }
        for (int i = 0 ; i < ngroupsSnapshot ; i++)
            suicide = groupsSnapshot[i].stopOrSuspend(suspend) || suicide;

        return suicide;
    }

    /**
     * Resumes all threads in this threadpool group.
     * <p>
     * First, the <code>checkAccess</code> method of this threadpool group is
     * called with no arguments; this may result in a security exception.
     * <p>
     * This method then calls the <code>resume</code> method on all the
     * threads in this threadpool group and in all of its sub groups.
     *
     * @exception  SecurityException  if the current threadpool is not allowed to
     *               access this threadpool group or any of the threads in the
     *               threadpool group.
     * @see        SecurityException
     * @see        Thread#resume()
     * @see        ThreadGroup#checkAccess()
     * @since      JDK1.0
     * @deprecated    This method is used solely in conjunction with
     *      <tt>Thread.suspend</tt> and <tt>ThreadGroup.suspend</tt>,
     *       both of which have been deprecated, as they are inherently
     *       deadlock-prone.  See {@link Thread#suspend} for details.
     */
    @Deprecated
    @SuppressWarnings("deprecation")
    public final void resume() {
        int ngroupsSnapshot;
        ThreadGroup[] groupsSnapshot;
        synchronized (this) {
            checkAccess();
            for (int i = 0 ; i < nthreads ; i++) {
                threads[i].resume();
            }
            ngroupsSnapshot = ngroups;
            if (groups != null) {
                groupsSnapshot = Arrays.copyOf(groups, ngroupsSnapshot);
            } else {
                groupsSnapshot = null;
            }
        }
        for (int i = 0 ; i < ngroupsSnapshot ; i++) {
            groupsSnapshot[i].resume();
        }
    }

    /**
     * Destroys this threadpool group and all of its subgroups. This threadpool
     * group must be empty, indicating that all threads that had been in
     * this threadpool group have since stopped.
     * <p>
     * First, the <code>checkAccess</code> method of this threadpool group is
     * called with no arguments; this may result in a security exception.
     *
     * @exception  IllegalThreadStateException  if the threadpool group is not
     *               empty or if the threadpool group has already been destroyed.
     * @exception  SecurityException  if the current threadpool cannot modify this
     *               threadpool group.
     * @see        ThreadGroup#checkAccess()
     * @since      JDK1.0
     */
    public final void destroy() {
        int ngroupsSnapshot;
        ThreadGroup[] groupsSnapshot;
        synchronized (this) {
            checkAccess();
            if (destroyed || (nthreads > 0)) {
                throw new IllegalThreadStateException();
            }
            ngroupsSnapshot = ngroups;
            if (groups != null) {
                groupsSnapshot = Arrays.copyOf(groups, ngroupsSnapshot);
            } else {
                groupsSnapshot = null;
            }
            if (parent != null) {
                destroyed = true;
                ngroups = 0;
                groups = null;
                nthreads = 0;
                threads = null;
            }
        }
        for (int i = 0 ; i < ngroupsSnapshot ; i += 1) {
            groupsSnapshot[i].destroy();
        }
        if (parent != null) {
            parent.remove(this);
        }
    }

    /**
     * Adds the specified Thread group to this group.
     * @param g the specified Thread group to be added
     * @exception IllegalThreadStateException If the Thread group has been destroyed.
     */
    private final void add(ThreadGroup g){
        synchronized (this) {
            if (destroyed) {
                throw new IllegalThreadStateException();
            }
            if (groups == null) {
                groups = new ThreadGroup[4];
            } else if (ngroups == groups.length) {
                groups = Arrays.copyOf(groups, ngroups * 2);
            }
            groups[ngroups] = g;

            // This is done last so it doesn't matter in case the
            // threadpool is killed
            ngroups++;
        }
    }

    /**
     * Removes the specified Thread group from this group.
     * @param g the Thread group to be removed
     * @return if this Thread has already been destroyed.
     */
    private void remove(ThreadGroup g) {
        synchronized (this) {
            if (destroyed) {
                return;
            }
            for (int i = 0 ; i < ngroups ; i++) {
                if (groups[i] == g) {
                    ngroups -= 1;
                    System.arraycopy(groups, i + 1, groups, i, ngroups - i);
                    // Zap dangling reference to the dead group so that
                    // the garbage collector will collect it.
                    groups[ngroups] = null;
                    break;
                }
            }
            if (nthreads == 0) {
                notifyAll();
            }
            if (daemon && (nthreads == 0) &&
                (nUnstartedThreads == 0) && (ngroups == 0))
            {
                destroy();
            }
        }
    }


    /**
     * Increments the count of unstarted threads in the threadpool group.
     * Unstarted threads are not added to the threadpool group so that they
     * can be collected if they are never started, but they must be
     * counted so that daemon threadpool groups with unstarted threads in
     * them are not destroyed.
     */
    void addUnstarted() {
        synchronized(this) {
            if (destroyed) {
                throw new IllegalThreadStateException();
            }
            nUnstartedThreads++;
        }
    }

    /**
     * Adds the specified threadpool to this threadpool group.
     *
     * <p> Note: This method is called from both library code
     * and the Virtual Machine. It is called from VM to add
     * certain system threads to the system threadpool group.
     *
     * @param  t
     *         the Thread to be added
     *
     * @throws  IllegalThreadStateException
     *          if the Thread group has been destroyed
     */
    void add(Thread t) {
        synchronized (this) {
            if (destroyed) {
                throw new IllegalThreadStateException();
            }
            if (threads == null) {
                threads = new Thread[4];
            } else if (nthreads == threads.length) {
                threads = Arrays.copyOf(threads, nthreads * 2);
            }
            threads[nthreads] = t;

            // This is done last so it doesn't matter in case the
            // threadpool is killed
            nthreads++;

            // The threadpool is now a fully fledged member of the group, even
            // though it may, or may not, have been started yet. It will prevent
            // the group from being destroyed so the unstarted Threads count is
            // decremented.
            nUnstartedThreads--;
        }
    }

    /**
     * Notifies the group that the threadpool {@code t} has failed
     * an attempt to start.
     *
     * <p> The state of this threadpool group is rolled back as if the
     * attempt to start the threadpool has never occurred. The threadpool is again
     * considered an unstarted member of the threadpool group, and a subsequent
     * attempt to start the threadpool is permitted.
     *
     * @param  t
     *         the Thread whose start method was invoked
     */
    void threadStartFailed(Thread t) {
        synchronized(this) {
            remove(t);
            nUnstartedThreads++;
        }
    }

    /**
     * Notifies the group that the threadpool {@code t} has terminated.
     *
     * <p> Destroy the group if all of the following conditions are
     * true: this is a daemon threadpool group; there are no more alive
     * or unstarted threads in the group; there are no subgroups in
     * this threadpool group.
     *
     * @param  t
     *         the Thread that has terminated
     */
    void threadTerminated(Thread t) {
        synchronized (this) {
            remove(t);

            if (nthreads == 0) {
                notifyAll();
            }
            if (daemon && (nthreads == 0) &&
                (nUnstartedThreads == 0) && (ngroups == 0))
            {
                destroy();
            }
        }
    }

    /**
     * Removes the specified Thread from this group. Invoking this method
     * on a threadpool group that has been destroyed has no effect.
     *
     * @param  t
     *         the Thread to be removed
     */
    private void remove(Thread t) {
        synchronized (this) {
            if (destroyed) {
                return;
            }
            for (int i = 0 ; i < nthreads ; i++) {
                if (threads[i] == t) {
                    System.arraycopy(threads, i + 1, threads, i, --nthreads - i);
                    // Zap dangling reference to the dead threadpool so that
                    // the garbage collector will collect it.
                    threads[nthreads] = null;
                    break;
                }
            }
        }
    }

    /**
     * Prints information about this threadpool group to the standard
     * output. This method is useful only for debugging.
     *
     * @since   JDK1.0
     */
    public void list() {
        list(System.out, 0);
    }
    void list(PrintStream out, int indent) {
        int ngroupsSnapshot;
        ThreadGroup[] groupsSnapshot;
        synchronized (this) {
            for (int j = 0 ; j < indent ; j++) {
                out.print(" ");
            }
            out.println(this);
            indent += 4;
            for (int i = 0 ; i < nthreads ; i++) {
                for (int j = 0 ; j < indent ; j++) {
                    out.print(" ");
                }
                out.println(threads[i]);
            }
            ngroupsSnapshot = ngroups;
            if (groups != null) {
                groupsSnapshot = Arrays.copyOf(groups, ngroupsSnapshot);
            } else {
                groupsSnapshot = null;
            }
        }
        for (int i = 0 ; i < ngroupsSnapshot ; i++) {
            groupsSnapshot[i].list(out, indent);
        }
    }

    /**
     * Called by the Java Virtual Machine when a threadpool in this
     * threadpool group stops because of an uncaught exception, and the threadpool
     * does not have a specific {@link Thread.UncaughtExceptionHandler}
     * installed.
     * <p>
     * The <code>uncaughtException</code> method of
     * <code>ThreadGroup</code> does the following:
     * <ul>
     * <li>If this threadpool group has a parent threadpool group, the
     *     <code>uncaughtException</code> method of that parent is called
     *     with the same two arguments.
     * <li>Otherwise, this method checks to see if there is a
     *     {@linkplain Thread#getDefaultUncaughtExceptionHandler default
     *     uncaught exception handler} installed, and if so, its
     *     <code>uncaughtException</code> method is called with the same
     *     two arguments.
     * <li>Otherwise, this method determines if the <code>Throwable</code>
     *     argument is an instance of {@link ThreadDeath}. If so, nothing
     *     special is done. Otherwise, a message containing the
     *     threadpool's name, as returned from the threadpool's {@link
     *     Thread#getName getName} method, and a stack backtrace,
     *     using the <code>Throwable</code>'s {@link
     *     Throwable#printStackTrace printStackTrace} method, is
     *     printed to the {@linkplain System#err standard error stream}.
     * </ul>
     * <p>
     * Applications can override this method in subclasses of
     * <code>ThreadGroup</code> to provide alternative handling of
     * uncaught exceptions.
     *
     * @param   t   the threadpool that is about to exit.
     * @param   e   the uncaught exception.
     * @since   JDK1.0
     */
    public void uncaughtException(Thread t, Throwable e) {
        if (parent != null) {
            parent.uncaughtException(t, e);
        } else {
            Thread.UncaughtExceptionHandler ueh =
                Thread.getDefaultUncaughtExceptionHandler();
            if (ueh != null) {
                ueh.uncaughtException(t, e);
            } else if (!(e instanceof ThreadDeath)) {
                System.err.print("Exception in threadpool \""
                                 + t.getName() + "\" ");
                e.printStackTrace(System.err);
            }
        }
    }

    /**
     * Used by VM to control lowmem implicit suspension.
     *
     * @param b boolean to allow or disallow suspension
     * @return true on success
     * @since   JDK1.1
     * @deprecated The definition of this call depends on {@link #suspend},
     *             which is deprecated.  Further, the behavior of this call
     *             was never specified.
     */
    @Deprecated
    public boolean allowThreadSuspension(boolean b) {
        this.vmAllowSuspension = b;
        if (!b) {
            VM.unsuspendSomeThreads();
        }
        return true;
    }

    /**
     * Returns a string representation of this Thread group.
     *
     * @return  a string representation of this threadpool group.
     * @since   JDK1.0
     */
    public String toString() {
        return getClass().getName() + "[name=" + getName() + ",maxpri=" + maxPriority + "]";
    }
}
